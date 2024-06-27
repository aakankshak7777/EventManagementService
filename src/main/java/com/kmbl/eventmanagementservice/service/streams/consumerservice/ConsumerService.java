package com.kmbl.eventmanagementservice.service.streams.consumerservice;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerService {

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

    private void validateRequest(CBSTransactionLogs cbsTransactionLogs)
    {
        if(cbsTransactionLogs.getAfter() == null)
        {
            throw new IllegalArgumentException("After State can't be null");
        }
    }
}
