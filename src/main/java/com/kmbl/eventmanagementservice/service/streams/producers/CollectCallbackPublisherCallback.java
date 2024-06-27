package com.kmbl.eventmanagementservice.service.streams.producers;

import com.kmbl.eventmanagementservice.model.CollectCallbackEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class CollectCallbackPublisherCallback implements KafkaPublisher.CommitCallback<CollectCallbackEvent> {

    @Override
    public void handle(KafkaPublisher.MessageBox<CollectCallbackEvent> message, RecordMetadata metadata, Exception exception) {
        System.out.println(message.getMessage());
        log.info(
                "Received callback. Trace Epochs: {}, {}, {}, {}",
                message.getEpochs(),
                kv("TxnId", message.getMessage()),
                kv("Offset", metadata.offset()),
                kv("SerValSize", metadata.serializedValueSize()));
    }
}
