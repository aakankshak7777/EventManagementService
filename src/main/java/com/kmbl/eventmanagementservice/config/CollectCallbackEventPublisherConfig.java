package com.kmbl.eventmanagementservice.config;

import com.kmbl.eventmanagementservice.model.CollectCallback;
import com.kmbl.eventmanagementservice.model.CollectCallbackEvent;
import com.kmbl.eventmanagementservice.service.streams.producers.CollectCallbackPublisherCallback;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaProducerFactory;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaPublisher;
import com.kmbl.eventmanagementservice.service.streams.serializers.CollectCallbackEventSerializer;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import io.micrometer.core.instrument.MeterRegistry;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;

@Component
public class CollectCallbackEventPublisherConfig  {

    @Value("${rts-transactions.kafka.consumer.bootstrap-servers}")
    private String collectCallbackEventsKafkaBootstrapServers;

    @Value("${rts-transactions.kafka.consumer.topic}")
    private String transactionsKafkaTopicName;

    private int kafkaPublishersCount = 5;

    private int callbackThreadsCount = 5;

    @Value("${rts-transactions.kafka.consumer.ssl-trust-store-location:#{null}}")
    private File kafkaSslTrustStoreLocation;

    @Value("${rts-transactions.kafka.consumer.security-protocol:#{null}}")
    private String securityProtocol;

    @Bean
    public CollectCallbackPublisherCallback callback() {
        return new CollectCallbackPublisherCallback();
    }

    @Bean
    public KafkaPublisher<CollectCallbackEvent> transactionsKafkaPublisher(
            CollectCallbackPublisherCallback callback, EpochProvider epochProvider) {
        var producerFactory = new KafkaProducerFactory<CollectCallbackEvent>(
                transactionsKafkaTopicName,
                collectCallbackEventsKafkaBootstrapServers,
                kafkaSslTrustStoreLocation,
                securityProtocol,
                CollectCallbackEventSerializer.class);
        return new KafkaPublisher<>(
                t -> String.join(":",t.partitionKey(), t.partitionKey()),
                kafkaPublishersCount,
                epochProvider,
                callback,
                callbackThreadsCount,
                producerFactory);
    }
}