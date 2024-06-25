 package com.kmbl.eventmanagementservice.config;


 import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
 import com.kmbl.eventmanagementservice.service.streams.CBSTranLogConsumer;
  import com.kmbl.eventmanagementservice.service.streams.consumers.ConsumerConfiguration;
 import com.kmbl.eventmanagementservice.service.streams.consumers.DlqPublisher;
 import com.kmbl.eventmanagementservice.service.streams.consumers.ReactiveKafkaConsumer;
 import io.micrometer.core.instrument.MeterRegistry;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Qualifier;
 import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
 import org.springframework.boot.context.properties.ConfigurationProperties;
 import org.springframework.context.annotation.Bean;
 import org.springframework.stereotype.Component;

 @ConditionalOnProperty(prefix = "app", name = "cbs-transactions-consumer-enabled", havingValue = "true")
 @Component
 @RequiredArgsConstructor
 public class KafkaConsumerConfig {
    @Bean
    @ConfigurationProperties(prefix = "cbs-tran-log-events.kafka.consumer")
    @Qualifier("cbsTransactionLogsKafka")
    public ConsumerConfiguration<CBSTransactionLogs> cbsTransactionLogsConsumerConfiguration(
            CBSTranLogConsumer cbsTranLogConsumer, DlqPublisher<CBSTransactionLogs> dlqPublisher) {
        var config = new ConsumerConfiguration<CBSTransactionLogs>();
        config.setProcessor(cbsTranLogConsumer);
        config.setDlqPublisher(dlqPublisher);
        return config;
    }

    @Bean
    @Qualifier("cbsTransactionLogsReactiveKafkaConsumer")
    public ReactiveKafkaConsumer<CBSTransactionLogs> cbsTransactionLogsReactiveKafkaConsumer(
            @Qualifier("cbsTransactionLogsKafka") ConsumerConfiguration<CBSTransactionLogs> config,
            MeterRegistry meterRegistry) {
        var consumer = new ReactiveKafkaConsumer<>(config, meterRegistry,false);
        consumer.start();
        return consumer;
    }
 }
