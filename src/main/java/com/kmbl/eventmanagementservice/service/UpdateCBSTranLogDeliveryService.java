package com.kmbl.eventmanagementservice.service;

import com.kmbl.eventmanagementservice.dao.CBSTranLogDao;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.model.CBSTranLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateCBSTranLogDeliveryService
{
    private final CBSTranLogDao dao;

    public UpdateCBSTranLogDeliveryService(CBSTranLogDao dao) {
        this.dao = dao;
    }

    public boolean updateCallbackEvent(CBSTranLogEvent cbsTranLogEvent, EventStatus eventStatus) {
        var cbsTranLog = cbsTranLogEvent.toCBSTranLog(eventStatus);
        try  {
            dao.update(cbsTranLog);
        } catch (Exception e) {
            log.info("Failed to update record {}: ",  cbsTranLog);
            return false;
        }
        return true;
    }
}
