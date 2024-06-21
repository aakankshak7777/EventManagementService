package com.kotak.merchant.payments.gateway.service.Event_Management_Service.service.streams.consumers;

import io.micrometer.core.instrument.Counter;
import jakarta.annotation.PreDestroy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import reactor.kafka.receiver.ReceiverRecord;

@Slf4j
public class DlqPublisher<T> {

    private final KafkaProducer<String, T> kafkaProducer;

    @Setter
    private Counter incomingMessagesCounter;

    @Setter
    private Counter publishingFailureCounter;

    private static final String DLQ_INCOMING_MESSAGES_COUNTER_NAME = "dlq.incoming.messages";
    private static final String DLQ_FAILED_TO_PUBLISH_COUNTER_NAME = "dlq.failed.to.publish";

    public DlqPublisher(KafkaProducer<String, T> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void publish(String topic, ReceiverRecord<String, T> event) {
        try {
            log.debug("Dlq publisher publishing event: {} to dlq: {}", event.value(), topic);
            incomingMessagesCounter.increment();
            var precord = new ProducerRecord<>(topic, event.key(), event.value());
            // todo: Get is blocking call. use call back mechanism here.
            kafkaProducer.send(precord).get();
            log.info("message published to DLQ {}", topic);
        } catch (Exception e) {
            // TODO : Implement mechanism with ReactiveKafkaConsumer in case of failure, so that we receive failed event
            // again.
            log.error("Exception in Failure handler ", e);
            publishingFailureCounter.increment();
        }
    }

    @PreDestroy
    public void shutdown() {
        if (kafkaProducer != null) {
            IOUtils.closeQuietly(kafkaProducer);
            log.info("dlq publisher shutdown");
        }
    }
}
