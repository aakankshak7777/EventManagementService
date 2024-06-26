package com.kmbl.eventmanagementservice.service.streams.consumerservice;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerService {
    public boolean printMessage(CBSTransactionLogs cbsTransactionLogs) {
        log.debug("cbsTransactionLogsEvent: {}", cbsTransactionLogs);

        try {
            System.out.println(cbsTransactionLogs);
            return true;
        } catch (Exception e) {
            log.warn("Skipping out of order update", e);
        }
        return false;
    }
}
