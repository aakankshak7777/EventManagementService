package com.kmbl.eventmanagementservice.service.event;

import com.kmbl.eventmanagementservice.service.dtos.CollectCallbackEvent;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaPublisher;
import javax.annotation.concurrent.ThreadSafe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ThreadSafe
@Service
public class CollectorCallbackEventService {
    private final KafkaPublisher<CollectCallbackEvent> kpublisher;

    public CollectorCallbackEventService(KafkaPublisher<CollectCallbackEvent> kpublisher) {
        this.kpublisher = kpublisher;
    }

    public boolean queueUp(CollectCallbackEvent collectCallbackEvent) {
        var submitted = kpublisher.offer(collectCallbackEvent);
        if (!submitted) {
            log.warn(
                    "Unable to submit message to publisher for pushing to Kafka: {}. "
                            + "This usually means that Kafka is down which is causing a backlog",
                    collectCallbackEvent);
        }
        return submitted;
    }
}
