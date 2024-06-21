package com.kotak.merchant.payments.gateway.service.Event_Management_Service.service.streams;

import com.kmbl.realtimetransactionservice.event.schema.RtsCbsGamEvents.GeneralAcctMastTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServiceExample {
    public boolean printMessage(GeneralAcctMastTable gamEvent) {
        log.debug("cbsGAMEvent: {}", gamEvent);

        try {
            System.out.println(gamEvent);
            return true;
        } catch (Exception e) {
            log.warn("Skipping out of order update", e);
        }
        return false;
    }
}
