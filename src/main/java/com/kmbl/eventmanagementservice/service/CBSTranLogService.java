package com.kmbl.eventmanagementservice.service;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogEvent;
import com.kmbl.eventmanagementservice.service.dtos.requests.CBSTranLogRequest;
import com.kmbl.eventmanagementservice.service.event.CBSTranLogGGEventService;
import javax.annotation.concurrent.ThreadSafe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ThreadSafe
@Service
public class CBSTranLogService {
        private final CBSTranLogGGEventService cbsTranLogGGEventService;

        public CBSTranLogService(CBSTranLogGGEventService cbsTranLogGGEventService) {
            this.cbsTranLogGGEventService = cbsTranLogGGEventService;
        }

        public void processCallbackEvent(CBSTranLogRequest request) {
            var cbsTranLogEvent = request.CBSTranLogEvent(EventName.COLLECT_CALLBACK_API);
            publishCBSTranLogEvent(cbsTranLogEvent);
            log.info("Event is processed " + cbsTranLogEvent);
        }

        private boolean publishCBSTranLogEvent(CBSTranLogEvent cbsTranLogEvent) {
            try {
                cbsTranLogGGEventService.queueUp(cbsTranLogEvent);
                return true;
            } catch (Exception e) {
                log.info("New event is not published with transactionId" + cbsTranLogEvent.txnId() + " and type "
                        + cbsTranLogEvent.type());
                return false;
            }
        }
    }
