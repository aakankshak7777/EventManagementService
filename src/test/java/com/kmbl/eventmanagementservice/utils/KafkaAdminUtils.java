package com.kmbl.eventmanagementservice.utils;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaAdminUtils {

    private static final Logger log = LoggerFactory.getLogger(KafkaAdminUtils.class);

    public static void createTopic(String bootstrapServers, String topic) {
        try {
            log.info("Creating Kafka topic with bootstrap servers: {}", bootstrapServers);

            int totalPartitions = 3;
            short replicationFactor = 1;
            Properties props = new Properties();
            props.put("bootstrap.servers", bootstrapServers);
            try (AdminClient adminClient = AdminClient.create(props)) {
                NewTopic newTopic = new NewTopic(topic, totalPartitions, replicationFactor);
                log.info("Creating topic: {} in Kafka", topic);
                adminClient.createTopics(List.of(newTopic)).all().get(); // Wait for creation to complete
            } catch (ExecutionException e) {
                if (e.getCause() instanceof TopicExistsException) {
                    log.info("Kafka topic: {} already exists.", topic);
                } else {
                    throw e;
                }
            }
            log.info("Finished creating Kafka topics");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteTopics(String bootstrapServers, String topic) {
        assert topic.startsWith("test-"); // Delete only test topics. Fail otherwise!

        try {
            log.info("Creating Kafka topic with bootstrap servers: {}", bootstrapServers);

            int totalPartitions = 3;
            short replicationFactor = 1;
            Properties props = new Properties();
            props.put("bootstrap.servers", bootstrapServers);
            try (AdminClient adminClient = AdminClient.create(props)) {
                NewTopic newTopic = new NewTopic(topic, totalPartitions, replicationFactor);
                log.info("Creating topic: {} in Kafka", topic);
                adminClient.createTopics(List.of(newTopic)).all().get(); // Wait for creation to complete
            } catch (ExecutionException e) {
                if (e.getCause() instanceof TopicExistsException) {
                    log.info("Kafka topic: {} already exists.", topic);
                } else {
                    throw e;
                }
            }
            log.info("Finished creating Kafka topics");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> getPartitions(String bootstrapServers, String topic) {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        try (AdminClient client = AdminClient.create(props)) {
            //      var result = client.describeTopics(List.of(topic)).allTopicNames().get();
            var result = client.describeTopics(List.of(topic)).all().get();
            var desc = result.get(topic);
            return desc.partitions().stream().map(TopicPartitionInfo::partition).toList();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
