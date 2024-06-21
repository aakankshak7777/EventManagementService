package com.kotak.merchant.payments.gateway.service.Event_Management_Service.config;

import com.kmbl.realtimetransactionservice.event.schema.RtsCbsGamEvents.GamColumns;
import com.kmbl.realtimetransactionservice.event.schema.RtsCbsGamEvents.GeneralAcctMastTable;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.service.streams.ConsumerKafkaExample;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.service.streams.consumers.ConsumerConfiguration;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.service.streams.consumers.DlqPublisher;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.service.streams.consumers.ReactiveKafkaConsumer;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.utils.EventsUtil;
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
public class CbsKafkaConsumerConfig {
    /* GAM configuration */
    @Bean
    @ConfigurationProperties(prefix = "rts-cbs-gam-events.kafka.consumer")
    @Qualifier("ggFinacleGAMKafka")
    public ConsumerConfiguration<GeneralAcctMastTable> ggFinacleGAMKafkaConfig(
            ConsumerKafkaExample cbsGAMConsumer, DlqPublisher<GeneralAcctMastTable> dlqPublisher) {
        var config = new ConsumerConfiguration<GeneralAcctMastTable>();
        config.setProcessor(cbsGAMConsumer);
        config.setDlqPublisher(dlqPublisher);
        config.setEventTimestampFunc(EventsUtil.generateTimestampFunc(
                GeneralAcctMastTable::getBefore,
                GeneralAcctMastTable::getAfter,
                GamColumns::getLCHGTIME,
                GeneralAcctMastTable::getOpTs));
        return config;
    }

    @Bean
    @Qualifier("ggFinacleGAMConsumer")
    public ReactiveKafkaConsumer<GeneralAcctMastTable> ggFinacleGAMConsumer(
            @Qualifier("ggFinacleGAMKafka") ConsumerConfiguration<GeneralAcctMastTable> config,
            MeterRegistry meterRegistry) {
        var consumer = new ReactiveKafkaConsumer<>(config, meterRegistry, false);
        consumer.start();
        return consumer;
    }
}
