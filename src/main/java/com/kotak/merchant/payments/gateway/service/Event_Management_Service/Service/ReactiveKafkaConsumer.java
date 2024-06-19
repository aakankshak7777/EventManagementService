// package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service;
//
// import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EMSMetricUtil;
// import io.micrometer.core.instrument.MeterRegistry;
// import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics;
// import java.time.Duration;
// import java.time.Instant;
// import java.util.List;
// import java.util.Optional;
// import java.util.Properties;
// import javax.annotation.Nullable;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.io.IOUtils;
// import org.apache.commons.lang3.Validate;
// import org.apache.kafka.clients.CommonClientConfigs;
// import org.apache.kafka.clients.consumer.Consumer;
// import org.apache.kafka.clients.consumer.ConsumerConfig;
// import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
// import org.apache.kafka.common.config.SaslConfigs;
// import org.apache.kafka.common.config.SslConfigs;
// import org.apache.kafka.common.serialization.StringDeserializer;
// import org.redisson.api.RedissonClient;
// import reactor.core.Disposable;
// import reactor.core.observability.micrometer.Micrometer;
// import reactor.core.scheduler.Scheduler;
// import reactor.core.scheduler.Schedulers;
// import reactor.kafka.receiver.KafkaReceiver;
// import reactor.kafka.receiver.ReceiverOptions;
// import reactor.kafka.receiver.ReceiverRecord;
// import reactor.kafka.receiver.internals.ConsumerFactory;
// import reactor.util.retry.Retry;
//
// @Slf4j
// public class ReactiveKafkaConsumer<T> extends Thread implements AutoCloseable {
//    private final ConsumerConfiguration<T> config;
//    private final KafkaReceiver<String, T> receiver;
//
//    @Nullable
//    private final EventConsumptionTracker<T> tracker;
//
//    private final MeterRegistry meterRegistry;
//
//    protected EMSMetricUtil metricUtil;
//
//    private Disposable flux;
//
//    private final boolean enableLagLog;
//
//    public ReactiveKafkaConsumer(ConsumerConfiguration<T> config, MeterRegistry meterRegistry) {
//        this(config, meterRegistry, null, null, true);
//    }
//
//    public ReactiveKafkaConsumer(
//            ConsumerConfiguration<T> config,
//            MeterRegistry meterRegistry,
//            @Nullable RedissonClient redisson,
//            @Nullable EpochProvider epochProvider,
//            boolean enableLagLog) {
//        this.config = config;
//        this.tracker = tryCreateTracker(config, redisson, epochProvider);
//        this.meterRegistry = meterRegistry;
//        this.metricUtil = new RtsMetricUtil(meterRegistry);
//        this.enableLagLog = enableLagLog;
//        Properties props = getKafkaConsumerProperties(config);
//        var commitIntervalMillis = config.getDeferredCommitConfig().getCommitIntervalMillis();
//
//        // https://dzone.com/articles/resilient-kafka-consumers-with-reactor-kafka
//        var options = ReceiverOptions.<String, T>create(props)
//                .subscription(List.of(config.getTopic()))
//                .maxDeferredCommits(config.getDeferredCommitConfig().getMaxDeferredCommits())
//                .commitInterval(Duration.ofMillis(commitIntervalMillis))
//                .commitBatchSize(config.getDeferredCommitConfig().getCommitBatchSize())
//                .addAssignListener(partitions -> log.info("Partitions assigned: {}", partitions))
//                .addRevokeListener(partitions -> log.info("Partitions revoked: {}", partitions));
//
//        this.receiver = KafkaReceiver.create(new ReactiveConsumerFactory(), options);
//    }
//
//    class ReactiveConsumerFactory extends ConsumerFactory {
//
//        @Override
//        public <K, V> Consumer<K, V> createConsumer(ReceiverOptions<K, V> config) {
//            var consumer = super.createConsumer(config);
//            var kmetrics = new KafkaClientMetrics(consumer);
//            kmetrics.bindTo(meterRegistry);
//            return consumer;
//        }
//    }
//
//    public Optional<EventConsumptionTracker<T>> tracker() {
//        return Optional.ofNullable(tracker);
//    }
//
//    @Override
//    public void run() {
//        log.info("Kickstarting Kafka consumer for topic: {}", config.getTopic());
//        var scheduler = scheduler();
//        flux = receiver.receive()
//                .doOnError(e -> log.error("Error receiving event in Kafka consumer. Will retry", e))
//                .retryWhen(Retry.fixedDelay(60L, Duration.ofMinutes(1)))
//                .groupBy(r -> inMemoryPartitionId(
//                        config.getProcessor().partitionKey(r.value()), config.getInMemoryPartitions()))
//                .flatMap(pf -> pf.publishOn(scheduler).map(this::processRecord))
//                .subscribe();
//        log.info("Kafka consumer stopped for topic: {}", config.getTopic());
//    }
//
//    @SuppressWarnings("PMD.AvoidCatchingGenericException")
//    private ReceiverRecord<String, T> processRecord(ReceiverRecord<String, T> message) {
//        try (var ignored = new MdcPublisher(new KafkaMdc(config.getTopic(), message.key()))) {
//            try (var timeIt = metricUtil.timeIt("MessageProcessingLatency")) {
//                log.debug("Processing message: {} from topic: {}", message, config.getTopic());
//                try {
//                    var currInstant = Instant.now();
//                    if (message.value() != null && config.getEventTimestampFunc() != null) {
//                        var lag = config.getEventTimestampFunc()
//                                .apply(message.value())
//                                .map(et -> Duration.between(et, currInstant));
//                        long lagTime = lag.map(Duration::toMillis).orElse(0L);
//                        if (enableLagLog) {
//                            log.info("Lag from event timestamp to beginning of queue consumption: {} millis",
// lagTime);
//                        }
//                        timeIt.emitTimeMetric("MessageProcessingLag.EventTimeStamp", lagTime);
//                    }
//
//                    // This timestamp is set by either the producer or Kafka
//                    var kafkaTimestamp = Instant.ofEpochMilli(message.timestamp());
//                    var klag = Duration.between(kafkaTimestamp, currInstant);
//                    if (enableLagLog) {
//                        log.info("Lag from Kafka producer or topic to beginning of queue consumption: {} millis",
// klag);
//                    }
//                    timeIt.emitTimeMetric("MessageProcessingLag.KafkaProducer", klag.toMillis());
//                    config.getProcessor().process(message);
//
//                    if (tracker != null) {
//                        var marker = tracker.markConsumed(message);
//                        log.trace("Marker: {}", marker);
//                    }
//                } catch (Exception e) {
//                    log.error(
//                            "Error processing event with key: {}, value: {} from topic: {}",
//                            message.key(),
//                            message.value(),
//                            config.getTopic(),
//                            e);
//                    if (config.getDlqPublisher() != null) {
//                        config.getDlqPublisher().publish(config.getDlqTopic(), message);
//                    } else {
//                        // TODO : not to acknowledge message in case of failed to push in DLQ topic.
//                        log.error("DLQ Failure Handler is not configured for topic: {}", config.getTopic());
//                    }
//                }
//                message.receiverOffset().acknowledge();
//                log.trace("Finished processing message: {} from topic: {}", message, config.getTopic());
//                return message;
//            }
//        }
//    }
//
//    private Scheduler scheduler() {
//        var scheduler = Schedulers.newBoundedElastic(
//                config.getInMemoryPartitions(),
//                Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE,
//                config.getProcessorThreadPoolName());
//        return Micrometer.timedScheduler(scheduler, meterRegistry, config.getTopic() + "-consumers-scheduler-");
//    }
//
//    @SuppressWarnings("PMD.AvoidCatchingGenericException")
//    @Override
//    public void close() {
//        log.info("Received signal to close ReactiveKafkaConsumer for topic: {}", config.getTopic());
//        try {
//            this.flux.dispose();
//        } catch (Exception e) {
//            log.info("Ignoring error while disposing consumer flux for topic: {}", config.getTopic(), e);
//        }
//        IOUtils.closeQuietly(tracker);
//    }
//
//    private static <T> Properties getKafkaConsumerProperties(ConsumerConfiguration<T> config) {
//        Properties props = new Properties();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getGroupId());
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.put(
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                config.getValueDeserializer().getName());
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
//        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
//        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.getMaxPollRecords());
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        // By default, Kafka uses the RangeAssignor for distributing partitions among consumers.
//        // This approach is eager when it comes to re-balancing, meaning it completely revokes all
//        // current assignments (which consumer handles which partition)
//        // whenever a new consumer joins or leaves the group.
//        // This can be disruptive,so a Cooperative re-balancing strategy is preferred.
//        // WARNING!: Now, changing strategy from co-operative to any other, require rolling bounce update.
//        // Avoid updating it without rolling bounce understanding.
//        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());
//        if (config.getSslTrustStoreLocation() != null) {
//            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
//            props.put(
//                    SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
//                    config.getSslTrustStoreLocation().toPath().toString());
//        }
//
//        if ("AWS_MSK_IAM".equals(config.getSecurityProtocol())) {
//            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
//            props.put(SaslConfigs.SASL_MECHANISM, "AWS_MSK_IAM");
//            props.put(SaslConfigs.SASL_JAAS_CONFIG, "software.amazon.msk.auth.iam.IAMLoginModule required;");
//            props.put(
//                    SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS,
//                    "software.amazon.msk.auth.iam.IAMClientCallbackHandler");
//        }
//        log.info("Kafka Consumer Properties: {}", props);
//        return props;
//    }
//
//    private static <T> EventConsumptionTracker<T> tryCreateTracker(
//            ConsumerConfiguration<T> config, @Nullable RedissonClient redisson, @Nullable EpochProvider epochProvider)
// {
//        if (config.getEventTimestampFunc() == null) {
//            log.info("Not creating event consumption tracker for topic: {}", config.getTopic());
//            return null;
//        }
//
//        Validate.notNull(redisson, "Redisson has to be specified if tracking is enabled");
//        Validate.notNull(epochProvider, "Epoch provider has to be specified if " + "tracking is enabled");
//        log.info("Creating event consumption tracker for topic: {}", config.getTopic());
//        return new EventConsumptionTracker<>(
//                redisson,
//                config.getTopic(),
//                epochProvider,
//                Duration.ofMillis(config.getTrackerFlushFrequencyMillis()),
//                config.getEventTimestampFunc());
//    }
// }
