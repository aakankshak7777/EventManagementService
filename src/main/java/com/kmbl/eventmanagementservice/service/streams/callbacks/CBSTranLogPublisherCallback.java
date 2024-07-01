package com.kmbl.eventmanagementservice.service.streams.callbacks;

import static net.logstash.logback.argument.StructuredArguments.kv;

import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.service.UpdateCBSTranLogDeliveryService;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogEvent;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
public class CBSTranLogPublisherCallback implements KafkaPublisher.CommitCallback<CBSTranLogEvent> {

    private final UpdateCBSTranLogDeliveryService updateCBSTranLogDeliveryService;

    public CBSTranLogPublisherCallback(UpdateCBSTranLogDeliveryService updateCBSTranLogDeliveryService) {
        this.updateCBSTranLogDeliveryService = updateCBSTranLogDeliveryService;
    }

    @Override
    public void handle(KafkaPublisher.MessageBox<CBSTranLogEvent> message, RecordMetadata metadata, Exception exception) {
        updateCBSTranLogDeliveryService.updateCallbackEvent(message.getMessage(), EventStatus.DELIVERED);
        System.out.println(message.getMessage());
        log.info(
                "Received callback. Trace Epochs: {}, {}, {}, {}",
                message.getEpochs(),
                kv("TxnId", message.getMessage()),
                kv("Offset", metadata.offset()),
                kv("SerValSize", metadata.serializedValueSize()));
    }
}