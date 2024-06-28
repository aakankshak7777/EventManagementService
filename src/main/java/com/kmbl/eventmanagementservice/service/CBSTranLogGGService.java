package com.kmbl.eventmanagementservice.service;

import com.kmbl.eventmanagementservice.dao.CBSTranLogGGDao;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.exceptions.CollectCallbackExistsException;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogGG;
import com.kmbl.eventmanagementservice.service.requests.CBSTranLogGGRequest;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import javax.annotation.concurrent.ThreadSafe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ThreadSafe
@Service
public class CBSTranLogGGService {

    private final CBSTranLogGGDao dao;
    private final EpochProvider epochProvider;
    public CBSTranLogGGService(CBSTranLogGGDao dao) {
        this.dao = dao;
        this.epochProvider = new EpochProvider();
    }

    public void create(CBSTranLogGGRequest request) {
        var currentTime = epochProvider.currentEpoch();
        CBSTranLogGG cbsTranLogGG= request.toCBSTranLog(EventStatus.DELIVERED);
        try {
            dao.create(cbsTranLogGG);
        } catch (CollectCallbackExistsException e) {
            var existing = dao.getByTransactionIdAndType(request.transactionId(), request.type())
                    .orElseThrow(() -> e);
            log.info("there is already a event with transactionId" + request.transactionId() + " and type "
                    + request.type());
            log.info("new event is not published with transactionId" + request.transactionId() + " and type "
                    + request.type());
        }
        log.info("event is published with transactionId" + request.transactionId() + " and type " + request.type());
    }
}