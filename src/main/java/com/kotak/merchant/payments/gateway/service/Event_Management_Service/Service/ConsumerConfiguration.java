 package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service;

 import io.micrometer.core.instrument.MeterRegistry;
 import java.io.File;
 import java.time.Instant;
 import java.util.Optional;
 import java.util.function.Function;
 import javax.annotation.Nullable;
 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 import org.apache.kafka.common.serialization.Deserializer;

 @Builder
 @Data
 @NoArgsConstructor
 @AllArgsConstructor
 public class ConsumerConfiguration<T> {

    /**
     * Kafka server endpoints
     */
    private String bootstrapServers;

    /**
     * Kafka topic to consume messages from
     */
    private String topic;

    /**
     * Group ID to use for consumers
     */
    private String groupId;

    /**
     * Class to use for deserializing a Kafka message
     */
    private Class<? extends Deserializer<T>> valueDeserializer;

    /**
     * When the latency of message consumption is high, it can result in backlogs accumulating in the
     * partition with the consumer never being able to catch up. To get around this, we need to
     * process multiple messages from the same partition in parallel.
     * <p>
     * An important assumption here is that the messages in a Kafka topic partition do not all
     * necessarily belong to the same logical partition, and the cardinality of logical partitions is
     * much higher than the number of physical partitions. Ex: The logical partition may be customer
     * account (order of tens of millions), while the physical partition may be 1000.
     * <p>
     * So, we use the logical partition key to build another set of in-memory partitions that helps
     * parallelize message processing within a Kafka partition without losing ordering within a
     * message's logical partition.
     * <p>
     * This controls the level of parallelism for this consumer.
     */
    private int inMemoryPartitions;

    /**
     * Maximum number of records to return from a Kafka consumer poll() call.
     * <p>
     * Note that the Kafka consumer may still retrieve a lot more messages defined by the maximum
     * fetch per partition and the total maximum fetch size across topics/partitions. These additional
     * messages are buffered in memory and returned on subsequent poll() calls.
     */
    private int maxPollRecords;

    /**
     * The name to use for the thread pool whose threads are used to execute the specified message
     * processor
     */
    private String processorThreadPoolName;

    /**
     * The message consumer that processes each message.
     * <p>
     * The processor can invoke blocking APIs and be generally slow as long as inMemoryPartitions is
     * large enough to achieve the required throughput.
     * <p>
     * No exceptions can be raised from the consumer. If there are any exceptions in handling the
     * message, the processor is expected to finish retrying and sidelining as well if the error
     * persists.
     * <p>
     * In future releases, we will consider supporting a separate error handler.
     */
    private MessageConsumer<T> processor;

    /**
     * publish the failed events during process.
     */
    private DlqPublisher<T> dlqPublisher;

    /**
     * dead-letter queue topic name, The failed consumer events will be published to.
     */
    private String dlqTopic;

    private DeferredCommitConfiguration deferredCommitConfig;

    /**
     * Security protocol to use for Kafka. Can be ssl or blank. If blank, PLAINTEXT is used
     */
    private String securityProtocol;

    /**
     * File path of SSL trust store
     */
    private File sslTrustStoreLocation;

    /**
     * File path of SSL Key store
     */
    private File sslKeyStoreLocation;

    /**
     * Function to obtain event timestamp to be used by the tracker.  (optional)
     */
    @Nullable
    private Function<T, Optional<Instant>> eventTimestampFunc;

    /**
     * Frequency of flushing event consumption timestamp (milliseconds). This controls how frequently
     * timestamps of consumed events are flushed to the backing datastore. The lag monitor uses only
     * the values flushed to the datastore. Optional, but has to be specified if eventTimestampFunc is
     * specified.
     */
    private Long trackerFlushFrequencyMillis;

    private MeterRegistry meterRegistry;

    /**
     * Using in-memory partitions means that messages may not be processed in the same order they
     * appear in the Kafka partition. If we blindly commit messages as we process them, a later
     * message M2 in the log may be committed before an earlier message M1 completes processing; the
     * implication is that if the consumer dies before M1 is processed, it will resume after M2
     * because that is the latest committed offset! So, we need to track message offsets and
     * periodically commit the earliest contiguous offset. Reactor Kafka does this for us
     * automatically.
     * <p>
     * See <a href="https://projectreactor.io/docs/kafka/release/reference/index.html">Section
     * 5.3.5</a> here for more details.
     * <p>
     * This configuration holds properties related to deferred commits.
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeferredCommitConfiguration {

        /**
         * Maximum number of messages that are not yet committed after which we pause the consumer to
         * apply backpressure.
         */
        private int maxDeferredCommits;

        /**
         * Frequency of committing the earliest contiguous offset (if possible) since the last commit
         * for a partition.
         * <p>
         * A commit is triggered (if possible) when either the commitIntervalMillis timer expires or the
         * batch size is exceeded.
         */
        private long commitIntervalMillis;

        /**
         * Maximum batch size after which the earliest contiguous offset (if possible) since the last
         * commit for a partition is committed.
         */
        private int commitBatchSize;
    }
 }

