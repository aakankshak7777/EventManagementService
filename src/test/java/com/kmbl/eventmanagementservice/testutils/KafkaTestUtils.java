package com.kmbl.eventmanagementservice.testutils;


import static com.kmbl.eventmanagementservice.testutils.RandUtils.randStr;

import com.kmbl.eventmanagementservice.service.streams.MessageConsumer;
import com.kmbl.eventmanagementservice.service.streams.consumers.ConsumerConfiguration;
import com.kmbl.eventmanagementservice.service.streams.consumers.DlqPublisher;
import com.kmbl.eventmanagementservice.service.streams.consumers.ReactiveKafkaConsumer;
import com.kmbl.eventmanagementservice.utils.AvroSerializerUtil;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringSerializer;


@Slf4j
public class KafkaTestUtils<T> {

    ReactiveKafkaConsumer<T> newConsumer(
            String bootstrapServers,
            String topic,
            String groupId,
            MessageConsumer<T> processor,
            Class<? extends Deserializer<T>> deserlizer,
            String dlqTopic,
            DlqPublisher<T> failureHandler) {

        var config = getConsumerConfiguration(
                bootstrapServers, topic, groupId, processor, deserlizer, dlqTopic, failureHandler);
        var consumer = new ReactiveKafkaConsumer<>(config, new LoggingMeterRegistry());
        consumer.start();
        return consumer;
    }

    private static <T> ConsumerConfiguration<T> getConsumerConfiguration(
            String bootstrapServers,
            String topic,
            String groupId,
            MessageConsumer<T> processor,
            Class<? extends Deserializer<T>> deserlizer,
            String dlqTopic,
            DlqPublisher<T> dlqPublisher) {

        return ConsumerConfiguration.<T>builder()
                .bootstrapServers(bootstrapServers)
                .topic(topic)
                .groupId(groupId)
                .maxPollRecords(50)
                .inMemoryPartitions(5)
                .processorThreadPoolName(randStr(24))
                .deferredCommitConfig(ConsumerConfiguration.DeferredCommitConfiguration.builder()
                        .commitIntervalMillis(3000L)
                        .commitBatchSize(5)
                        .maxDeferredCommits(5)
                        .build())
                .valueDeserializer(deserlizer)
                .dlqTopic(dlqTopic)
                .dlqPublisher(dlqPublisher)
                .processor(processor)
                .build();
    }

    public ReactiveKafkaConsumer<T> newConsumer(
            String bootstrapServers,
            String topic,
            String groupId,
            MessageConsumer<T> processor,
            Class<? extends Deserializer<T>> deserlizer) {
        return newConsumer(bootstrapServers, topic, groupId, processor, deserlizer, null, null);
    }

    public void publishMessages(String bootstrapServers, String topic, T message) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializerUtil.class.getName());
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
        props.put(ProducerConfig.ACKS_CONFIG, "1");

        try (KafkaProducer<String, T> producer = new KafkaProducer<>(props)) {
            producer.send(message(topic, message), ((metadata, exception) -> {
                if (exception == null) {
                    log.info("Received new metadata. \n" + "Topic:" + metadata.topic() + "\n" + "Partition: "
                            + metadata.partition() + "\n" + "Offset: " + metadata.offset() + "\n" + "Timestamp: "
                            + metadata.timestamp());
                }
            }));
            producer.flush();
        }
    }

    private ProducerRecord<String, T> message(String topic, T p) {
        return new ProducerRecord<>(topic, "", p);
    }
}
