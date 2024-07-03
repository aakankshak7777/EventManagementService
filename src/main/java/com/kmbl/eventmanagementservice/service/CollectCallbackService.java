package com.kmbl.eventmanagementservice.service;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.service.dtos.CollectCallbackEvent;
import com.kmbl.eventmanagementservice.service.dtos.requests.CollectCallbackRequest;
import com.kmbl.eventmanagementservice.service.event.CollectorCallbackEventService;
import javax.annotation.concurrent.ThreadSafe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ThreadSafe
@Service
public class CollectCallbackService {
    private final CollectorCallbackEventService collectorCallbackEventService;

    public CollectCallbackService(CollectorCallbackEventService collectorCallbackEventService) {
        this.collectorCallbackEventService = collectorCallbackEventService;
    }

    public void processCallbackEvent(CollectCallbackRequest request) {
        var collectCallbackEvent = request.toCollectCallbackEvent(EventName.COLLECT_CALLBACK_API);
        publishCallbackEvent(collectCallbackEvent);
        log.info("Event is processed " + collectCallbackEvent);
    }

    private boolean publishCallbackEvent(CollectCallbackEvent collectCallbackEvent) {
        try {
            collectorCallbackEventService.queueUp(collectCallbackEvent);
            return true;
           } catch (Exception e) {
             log.info("New event is not published with transactionId" + collectCallbackEvent.transactionId() + " and type "
                + collectCallbackEvent.type());
             return false;
        }
    }
}
