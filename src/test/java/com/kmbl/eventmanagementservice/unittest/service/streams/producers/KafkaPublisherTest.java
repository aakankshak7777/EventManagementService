package com.kmbl.eventmanagementservice.unittest.service.streams.producers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


import com.kmbl.eventmanagementservice.service.streams.producers.KafkaProducerFactory;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaPublisher;
import com.kmbl.eventmanagementservice.unittest.service.streams.KafkaTestBase;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import com.kmbl.eventmanagementservice.utils.JsonSerializer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KafkaPublisherTest extends KafkaTestBase {

    private ConsumerLite consumer;
    private CallbackEventLiteCommitCallback callback;

    @BeforeEach
    public void setUp() {
        consumer = new ConsumerLite();
        callback = new CallbackEventLiteCommitCallback();
    }

    @AfterEach
    public void cleanUp() {
        consumer.close();
    }

    @Test
    public void testOffer_AcceptsMessage() {

        try (var publisher = newPublisher()) {
            var messages = generateForCallBackEvents(5, 5);
            var result = messages.stream().flatMap(acc -> acc.stream().map(publisher::offer));
            assertThat(result.allMatch(r -> r.equals(true))).isTrue();
        }
    }

    @Test
    public void testOffer_RejectsMessageIfProducerStopped() {
        KafkaPublisher<CallbackEventLite> publisher = newPublisher();
        publisher.close();

        var messages = generateForCallBackEvents(5, 5);
        var result = messages.stream().flatMap(acc -> acc.stream().map(publisher::offer));
        assertThat(result.allMatch(r -> r.equals(false))).isTrue();
    }

    @Test
    public void testPublish_PublishesMessagesInPartitionKeyOrder() {

        try (var publisher = newPublisher()) {
            var messages = generateForCallBackEvents(10, 5);
            var transactions = concat(messages);
            transactions.forEach(publisher::offer);

            await().atMost(Duration.ofSeconds(10)).until(() -> consumer.transactions.size() >= transactions.size());
            assertThat(groupByAccount(consumer.transactions)).isEqualTo(groupByAccount(transactions));
        }
    }

    @Test
    public void testPublish_PublishesInterleavedMessagesInPartitionKeyOrder() {

        try (var publisher = newPublisher()) {
            var messages = generateForCallBackEvents(10, 5);
            var transactions = interleave(messages);
            transactions.forEach(publisher::offer);

            await().atMost(Duration.ofSeconds(10)).until(() -> consumer.transactions.size() >= transactions.size());
            assertThat(groupByAccount(consumer.transactions)).isEqualTo(groupByAccount(transactions));
        }
    }

    @Test
    public void testPublish_DrainsMessagesAfterStopping() {
        List<CallbackEventLite> transactions;
        try (var publisher = newPublisher()) {
            var messages = generateForCallBackEvents(20, 5);
            transactions = interleave(messages);
            transactions.forEach(publisher::offer);
        } // Close publisher immediately forcing a flush

        await().atMost(Duration.ofSeconds(10)).until(() -> consumer.transactions.size() >= transactions.size());
        assertThat(groupByAccount(consumer.transactions)).isEqualTo(groupByAccount(transactions));
    }

    @Test
    public void testPublish_CallsCommitCallbackForAllMessages() {
        List<CallbackEventLite> transactions;
        try (var publisher = newPublisher()) {
            var messages = generateForCallBackEvents(10, 5);
            transactions = interleave(messages);
            transactions.forEach(publisher::offer);
        }

        assertThat(groupByAccountAsSet(callback.transactions)).isEqualTo(groupByAccountAsSet(transactions));
    }

    private KafkaPublisher<CallbackEventLite> newPublisher() {

        var producerFactory = new KafkaProducerFactory<CallbackEventLite>(
                topic, bootstrapServers, null, null, JsonSerializer.class);
        return new KafkaPublisher<>(
                CallbackEventLite::key,
                5,
                new EpochProvider(),
                callback,
                3,
                producerFactory);
    }

    public static class CallbackEventLiteCommitCallback implements KafkaPublisher.CommitCallback<CallbackEventLite> {

        private final List<CallbackEventLite> transactions = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void handle(KafkaPublisher.MessageBox<CallbackEventLite> box, RecordMetadata metadata, Exception exception) {
            transactions.add(box.getMessage());
        }
    }

    /**
     * Our own lightweight simple consumer implementation to keep testing independent of the more
     * complex ReactiveKafkaConsumer
     */
    public class ConsumerLite implements AutoCloseable, Runnable {

        private final KafkaConsumer<String, CallbackEventLite> consumer;

        private final List<CallbackEventLite> transactions;
        private volatile boolean stop = false;

        public ConsumerLite() {
            this.transactions = Collections.synchronizedList(new ArrayList<>());
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TransactionLiteDeserializer.class.getName());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(List.of(topic));

            new Thread(this).start();
        }

        @Override
        public void run() {
            while (!stop) {
                var records = consumer.poll(Duration.ofMillis(100L));
                records.forEach(r -> transactions.add(r.value()));
            }
            consumer.close();
        }

        public void close() {
            this.stop = true;
        }
    }
}

