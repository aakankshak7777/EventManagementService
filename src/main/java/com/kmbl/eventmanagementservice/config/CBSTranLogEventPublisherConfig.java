package com.kmbl.eventmanagementservice.config;

import com.kmbl.eventmanagementservice.service.UpdateCBSTranLogDeliveryService;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogEvent;
import com.kmbl.eventmanagementservice.service.streams.callbacks.CBSTranLogPublisherCallback;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaProducerFactory;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaPublisher;
import com.kmbl.eventmanagementservice.service.streams.serializers.CBSTranLogEventSerializer;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CBSTranLogEventPublisherConfig {

    @Value("${rts-transactions.kafka.consumer.bootstrap-servers}")
    private String cbsTranLogEventKafkaBootstrapServers;

    @Value("${rts-transactions.kafka.consumer.topic1}")
    private String cbsTranLogEventKafkaTopicName;

    private int kafkaPublishersCount = 5;

    private int callbackThreadsCount = 5;

    @Value("${rts-transactions.kafka.consumer.ssl-trust-store-location:#{null}}")
    private File kafkaSslTrustStoreLocation;

    @Value("${rts-transactions.kafka.consumer.security-protocol:#{null}}")
    private String securityProtocol;

    @Bean
    public CBSTranLogPublisherCallback cbsTranLogPublisherCallbackBean(UpdateCBSTranLogDeliveryService updateCBSTranLogDeliveryService) {
        return new CBSTranLogPublisherCallback(updateCBSTranLogDeliveryService);
    }

    @Bean
    public KafkaPublisher<CBSTranLogEvent> cbsTranLogEventKafkaPublisher(
            CBSTranLogPublisherCallback callback, EpochProvider epochProvider) {
        var producerFactory = new KafkaProducerFactory<CBSTranLogEvent>(
                cbsTranLogEventKafkaTopicName,
                cbsTranLogEventKafkaBootstrapServers,
                kafkaSslTrustStoreLocation,
                securityProtocol,
                CBSTranLogEventSerializer.class);
        return new KafkaPublisher<>(
                t -> String.join(":",t.partitionKey(), t.partitionKey()),
                kafkaPublishersCount,
                epochProvider,
                callback,
                callbackThreadsCount,
                producerFactory);
    }
}
