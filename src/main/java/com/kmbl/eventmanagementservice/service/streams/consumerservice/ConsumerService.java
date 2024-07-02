package com.kmbl.eventmanagementservice.service.streams.consumerservice;


import static com.kmbl.eventmanagementservice.service.dtos.CBSTranLogData.to;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.service.CBSTranLogService;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogData;
import com.kmbl.eventmanagementservice.service.dtos.requests.CBSTranLogRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {

    private final CBSTranLogService cbsTranLogService;
    public ConsumerService(CBSTranLogService cbsTranLogService) {
        this.cbsTranLogService = cbsTranLogService;
    }
    public void sendMessage(CBSTransactionLogs cbsTransactionLogs) {
        CBSTranLogData cbsTranLogData = to(cbsTransactionLogs);
        log.info("message send to kafka and saving it to db : ", cbsTranLogData);
        CBSTranLogRequest cbsTranLogGGRequest = CBSTranLogRequest.from(cbsTranLogData, EventName.GG_CBS_TRAN_LOG);
        cbsTranLogService.processCallbackEvent(cbsTranLogGGRequest);
    }

    private void validateRequest(CBSTransactionLogs cbsTransactionLogs)
    {
        if(cbsTransactionLogs.getAfter() == null)
        {
            throw new IllegalArgumentException("After State can't be null");
        }
    }
}
