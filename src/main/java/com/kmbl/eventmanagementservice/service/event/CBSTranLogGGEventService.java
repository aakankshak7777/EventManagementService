package com.kmbl.eventmanagementservice.service.event;

import com.kmbl.eventmanagementservice.model.CBSTranLogEvent;
import javax.annotation.concurrent.ThreadSafe;

import com.kmbl.eventmanagementservice.service.streams.producers.KafkaPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ThreadSafe
@Service
public class CBSTranLogGGEventService {
    private final KafkaPublisher<CBSTranLogEvent> kpublisher;

    public CBSTranLogGGEventService(KafkaPublisher<CBSTranLogEvent> kpublisher) {
        this.kpublisher = kpublisher;
    }

    public boolean queueUp(CBSTranLogEvent cbsTranLogEvent) {
        var submitted = kpublisher.offer(cbsTranLogEvent);
        if (!submitted) {
            log.warn(
                    "Unable to submit message to publisher for pushing to Kafka: {}. "
                            + "This usually means that Kafka is down which is causing a backlog",
                    cbsTranLogEvent);
        }
        return submitted;
    }
}