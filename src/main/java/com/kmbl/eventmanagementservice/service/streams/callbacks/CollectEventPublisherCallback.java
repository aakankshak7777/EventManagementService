package com.kmbl.eventmanagementservice.service.streams.callbacks;

import static net.logstash.logback.argument.StructuredArguments.kv;

import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.model.CollectCallbackEvent;
import com.kmbl.eventmanagementservice.service.UpdateCallbackDeliveryService;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
public class CollectEventPublisherCallback implements KafkaPublisher.CommitCallback<CollectCallbackEvent> {

    private final UpdateCallbackDeliveryService updateCallbackDeliveryService;

    public CollectEventPublisherCallback(UpdateCallbackDeliveryService updateCallbackDeliveryService) {
        this.updateCallbackDeliveryService = updateCallbackDeliveryService;
    }

    @Override
    public void handle(KafkaPublisher.MessageBox<CollectCallbackEvent> message, RecordMetadata metadata, Exception exception) {
        updateCallbackDeliveryService.updateCallbackEvent(message.getMessage(), EventStatus.DELIVERED);
        log.info(
                "Received callback. Trace Epochs: {}, {}, {}, {}",
                message.getEpochs(),
                kv("CollectCallbackEvent", message.getMessage()),
                kv("Offset", metadata.offset()),
                kv("SerValSize", metadata.serializedValueSize()));
    }
}
