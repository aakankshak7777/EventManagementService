package com.kmbl.eventmanagementservice.config;

import com.kmbl.eventmanagementservice.model.CollectCallbackEvent;
import com.kmbl.eventmanagementservice.service.UpdateCallbackDeliveryService;
import com.kmbl.eventmanagementservice.service.streams.callbacks.CollectEventPublisherCallback;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaProducerFactory;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaPublisher;
import com.kmbl.eventmanagementservice.service.streams.serializers.CollectCallbackEventSerializer;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CallbackEventPublisherConfig {

    @Value("${rts-transactions.kafka.consumer.bootstrap-servers}")
    private String callbackEventKafkaBootstrapServers;

    @Value("${rts-transactions.kafka.consumer.topic}")
    private String callbackEventKafkaTopicName;

    private int kafkaPublishersCount = 5;

    private int callbackThreadsCount = 5;

    @Value("${rts-transactions.kafka.consumer.ssl-trust-store-location:#{null}}")
    private File kafkaSslTrustStoreLocation;

    @Value("${rts-transactions.kafka.consumer.security-protocol:#{null}}")
    private String securityProtocol;

    @Bean
    public CollectEventPublisherCallback collectEventPublisherCallbackBean(UpdateCallbackDeliveryService UpdateCallbackDeliveryService) {
        return new CollectEventPublisherCallback(UpdateCallbackDeliveryService);
    }

    @Bean
    public KafkaPublisher<CollectCallbackEvent> callbackEventKafkaPublisher(
            CollectEventPublisherCallback callback, EpochProvider epochProvider) {
        var producerFactory = new KafkaProducerFactory<CollectCallbackEvent>(
                callbackEventKafkaTopicName,
                callbackEventKafkaBootstrapServers,
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