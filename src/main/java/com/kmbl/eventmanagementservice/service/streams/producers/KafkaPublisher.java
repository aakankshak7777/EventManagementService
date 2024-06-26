package com.kmbl.eventmanagementservice.service.streams.producers;

import static com.kmbl.eventmanagementservice.utils.ThreadUtils.newThreadFactory;

import com.kmbl.eventmanagementservice.service.PartitionedEvent;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import com.kmbl.eventmanagementservice.utils.PartitionUtils;
import com.kmbl.eventmanagementservice.utils.ThreadUtils;
import com.kmbl.eventmanagementservice.utils.mdc.KafkaMdc;
import com.kmbl.eventmanagementservice.utils.mdc.MdcPublisher;
import java.io.Closeable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;


/**
 * High throughput Kafka publisher that enables publishing to a Kafka cluster via multiple in-memory
 * partitions. Each in-memory partition is backed by a Kafka producer which drains it and persists
 * the messages in that partition to Kafka. This is useful to have low-latency, high-throughput
 * publishing when multiple threads are producing data upstream.
 * <p>
 * In-memory queues are needed to avoid blocking by the Kafka producer and to keep the handoff
 * lightweight. Within the KafkaProducer's send method, a bunch of things happen - interceptor
 * invocation, key serialization, value serialization, blocking on metadata fetch from cluster if
 * necessary, batch selection/creation. We don't want to be blocked on any of this!
 *
 * @param <T> Type of the message to be published
 */
@Slf4j
public class KafkaPublisher<T extends PartitionedEvent> implements Closeable, AutoCloseable {

    private final String topic;
    private final EpochProvider epochProvider;
    private final int inMemoryPartitions;
    private final List<Worker> workers;
    private final Function<T, String> keyFunc;
    private final ExecutorService workerExecutor;
    private final CommitCallback<T> commitCallback;

    private final ExecutorService callbackExecutor;


    /**
     * Create a new instance of Kafka publisher. This is a threadsafe instance that can be safely
     * invoked from mulitple threads.
     *
     * @param keyFunc            The function used to generate the key of the message while publishing
     *                           to the Kafka topic
     * @param inMemoryPartitions In-memory partitions and equivalent number of producers to use for
     *                           pushing messages to Kafka.
     * @param epochProvider      Epoch provider to look up current time
     * @param commitCallback     Callback that is called once a commit is attempted for a Kafka
     *                           message. Because the KafkaProducer's original commit callback is
     *                           invoked from I/O bound threads, any code there is expected to
     *                           complete very, very fast. To avoid complications, this callback is
     *                           scheduled from a separate thread pool. Callbacks are not called in
     *                           the original order of message submission. Callbacks are optional.
     *                           This can be null.
     * @param callbackThreads    Number of threads to be used for callbacks
     * @param producerFactory    Kafka producer factory to create a producer per in-memory partition
     */
    public KafkaPublisher(
            Function<T, String> keyFunc,
            int inMemoryPartitions,
            EpochProvider epochProvider,
            @Nullable CommitCallback<T> commitCallback,
            int callbackThreads,
            KafkaProducerFactory<T> producerFactory) {
        this.topic = producerFactory.topic();
        this.epochProvider = epochProvider;
        this.inMemoryPartitions = inMemoryPartitions;

        this.keyFunc = keyFunc;
        this.commitCallback = commitCallback;

        this.workerExecutor = Executors.newFixedThreadPool(this.inMemoryPartitions, newThreadFactory("rts-publisher"));
        this.workers = IntStream.range(0, this.inMemoryPartitions)
                .mapToObj(pid -> new Worker(pid, producerFactory.newProducer(pid)))
                .toList();
        this.workers.forEach(this.workerExecutor::submit);

        ExecutorService commitCallbackExecutor = null;
        if (commitCallback != null) {
            commitCallbackExecutor =
                    Executors.newFixedThreadPool(callbackThreads, newThreadFactory("rts-publisher-commit-callback"));
        }
        this.callbackExecutor = commitCallbackExecutor;

    }

    public boolean offer(T message) {
        var partitionKey = message.partitionKey();
        int partitionId = PartitionUtils.inMemoryPartitionId(partitionKey, inMemoryPartitions);
        return workers.get(partitionId).offer(message);
    }

    public int pendingMessages() {
        return workers.stream().mapToInt(w -> w.queue.size()).sum();
    }

    public boolean hasPendingMessages() {
        return workers.stream().anyMatch(w -> !w.queue.isEmpty());
    }


    @Override
    public void close() {
        log.info("Shutting down Kafka publisher for topic: {}", topic);
        workers.forEach(Worker::stop);

        log.info("Shutting down worker executor service for topic: {}", topic);
        ThreadUtils.shutdownExecutor(workerExecutor);

        log.info("Shutting down commit callback executor for topic: {}", topic);
        ThreadUtils.shutdownExecutor(callbackExecutor);

        log.info("Shutting down production tracker for topic: {}", topic);
    }

    public class Worker implements Runnable {

        private final int partitionId;

        private final LinkedBlockingQueue<MessageBox<T>> queue;
        private volatile boolean stop;

        private volatile boolean stopped;



        private final KafkaProducer<String, T> producer;

        private static final String RTS_PUBLISHED_TRANSACTION_COUNTER = "rts.published.transaction";

        public Worker(int partitionId, KafkaProducer<String, T> producer) {
            this.partitionId = partitionId;
            this.queue = new LinkedBlockingQueue<>();
            this.producer = producer;
        }

        public void stop() {
            stop = true;
        }

        public boolean offer(T message) {
            if (stop) {
                log.info("Rejected message: {} because stop signal received for publisher", message);
                return false;
            }
            return queue.offer(new MessageBox<>(message, epochProvider.now()));
        }

        @Override
        @SuppressWarnings("PMD.AvoidCatchingGenericException")
        public void run() {
            try {
                while (!stop || !queue.isEmpty()) {
                    logIfStopSignalled();
                    var box = queue.poll(10L, TimeUnit.MILLISECONDS);
                    Optional.ofNullable(box).ifPresent(this::handleMessage);
                }
            } catch (Exception e) {
                log.warn("Worker thread for in-memory partition: {} failed ", partitionId, e);
                throw new RuntimeException(e);
            } finally {
                cleanup();
                stopped = true;
            }
        }

        private void logIfStopSignalled() {
            if (stop) {
                log.info(
                        "Received stop message. Draining out queue for in-memory partition: {}. "
                                + "Pending messages: {}",
                        partitionId,
                        queue.size());
            }
        }

        @SuppressWarnings("PMD.AvoidCatchingGenericException")
        private void cleanup() {
            try {
                log.info("Flushing producer for in-memory partition: {}", partitionId);
                producer.flush();
            } catch (Exception e) {
                log.warn("Error while flushing producer for in-memory partition: {}", partitionId, e);
            }
            IOUtils.closeQuietly(producer);
            log.info(
                    "Producer for in-memory partition: {} flushed and closed. Pending messages: {}",
                    partitionId,
                    queue.size());
        }

        private void handleMessage(MessageBox<T> b) {
            try (var mdc = new MdcPublisher(new KafkaMdc(topic, null))) {
                log.debug(
                        "Processing message: {} on in-memory partition: {}. Pending: {}", b, partitionId, queue.size());
                var key = keyFunc.apply(b.message);
                mdc.addMkey(key);
                var epoch = epochProvider.now();
                var precord = new ProducerRecord<>(topic, key, b.message);
                producer.send(precord, (metadata, exception) -> {
                    try (var pmdc = new MdcPublisher(new KafkaMdc(topic, key))) {
                        var callbackEpoch = epochProvider.now();
                        if (exception == null) {
                            b.markAckedByKafka(callbackEpoch);
                            log.info(
                                    "Message with id: {} on in-memory partition: {} published to Kafka at: {}",
                                    b.message.uniqueId(),
                                    partitionId,
                                    metadata);
                        } else {
                            b.markRejectedByKafka(callbackEpoch);
                            log.error(
                                    "Error in publishing message: {} on in-memory partition: {} to Kafka."
                                            + " Ignoring",
                                    b,
                                    partitionId,
                                    exception);
                        }
                        invokeCommitCallback(b, metadata, exception);
                    }
                });
                b.markSubmittedToKafka(epoch);
            }
        }

        @SuppressWarnings("PMD.AvoidCatchingGenericException")
        private void invokeCommitCallback(MessageBox<T> b, RecordMetadata metadata, Exception exception) {
            if (commitCallback != null) {
                callbackExecutor.submit(() -> {
                    try {
                        commitCallback.handle(b, metadata, exception);
                    } catch (Exception e) {
                        log.warn("Ignoring error in callback of: {}", b, e);
                    }
                });
            }
        }
    }

    @Data
    public static class MessageBox<T> {

        private final MessageTraceEpochs epochs;

        private final T message;

        public MessageBox(T message, Instant currentEpoch) {
            this.message = message;
            this.epochs = new MessageTraceEpochs();
            this.epochs.offeredToInMemoryQueueAt = currentEpoch;
        }

        public void markSubmittedToKafka(Instant epoch) {
            this.epochs.submittedToKafkaAt = epoch;
        }

        public void markAckedByKafka(Instant epoch) {
            this.epochs.ackedByKafkaAt = epoch;
        }

        public void markRejectedByKafka(Instant epoch) {
            this.epochs.rejectedByKafkaAt = epoch;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageTraceEpochs {

        private Instant offeredToInMemoryQueueAt;
        private Instant submittedToKafkaAt;

        private Instant ackedByKafkaAt;
        private Instant rejectedByKafkaAt;
    }

    public interface CommitCallback<T> {

        void handle(MessageBox<T> box, RecordMetadata metadata, Exception exception);
    }
}
