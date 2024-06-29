package com.kmbl.eventmanagementservice.service;

import com.kmbl.eventmanagementservice.dao.CollectCallbackDao;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.model.CollectCallbackEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateDeliveryService
{

    private final CollectCallbackDao dao;

    public UpdateDeliveryService(CollectCallbackDao dao) {
        this.dao = dao;
    }

    public boolean updateCallbackEvent(CollectCallbackEvent collectCallbackEvent, EventStatus eventStatus) {
        var collectCallback = collectCallbackEvent.toCollectCallback(eventStatus);
        try  {
            dao.update(collectCallback);
        } catch (Exception e) {
            log.info("Failed to update record {}: ",  collectCallback);
            return false;
        }
        return true;
    }
}
