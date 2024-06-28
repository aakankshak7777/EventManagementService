package com.kmbl.eventmanagementservice.service.streams.consumerservice;

import static com.kmbl.eventmanagementservice.model.CBSTranLogData.to;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.model.CBSTranLogData;
import com.kmbl.eventmanagementservice.model.CBSTranLogGGEvent;
import com.kmbl.eventmanagementservice.service.CBSTranLogGGService;
import com.kmbl.eventmanagementservice.service.event.CBSTranLogGGEventService;
import com.kmbl.eventmanagementservice.service.requests.CBSTranLogGGRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {

    private final CBSTranLogGGEventService cbsTranLogGGEventService;
    private final CBSTranLogGGService cbsTranLogGGService;

    @Autowired
    public ConsumerService(CBSTranLogGGService cbsTranLogGGService, CBSTranLogGGEventService cbsTranLogGGEventService) {
        this.cbsTranLogGGService = cbsTranLogGGService;
        this.cbsTranLogGGEventService = cbsTranLogGGEventService;
    }

    // Todo add logic here
    public boolean printMessage(CBSTransactionLogs cbsTransactionLogs) {
        log.debug("cbsTransactionLogsEvent: {}", cbsTransactionLogs);
         validateRequest(cbsTransactionLogs);
        try {
            System.out.println(cbsTransactionLogs);
            return true;
        } catch (Exception e) {
            log.warn("Skipping out of order update", e);
        }
        return false;
    }

    public void sendMessage(CBSTransactionLogs cbsTransactionLogs) {
        CBSTranLogData cbsTranLogData = to(cbsTransactionLogs);
        log.info("message send to kafka and saving it to db : ", cbsTranLogData);
        CBSTranLogGGRequest cbsTranLogGGRequest = CBSTranLogGGRequest.from(cbsTranLogData, EventName.GG_CBS_TRAN_LOG);
        cbsTranLogGGService.create(cbsTranLogGGRequest);
        CBSTranLogGGEvent cbsTranLogGGEvent = CBSTranLogGGEvent.from(cbsTranLogData);
        cbsTranLogGGEventService.publishMessage(cbsTranLogGGEvent);

    }

    private void validateRequest(CBSTransactionLogs cbsTransactionLogs)
    {
        if(cbsTransactionLogs.getAfter() == null)
        {
            throw new IllegalArgumentException("After State can't be null");
        }
    }
}
