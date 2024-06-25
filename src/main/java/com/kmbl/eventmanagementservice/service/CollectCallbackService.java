package com.kmbl.eventmanagementservice.service;

import com.kmbl.eventmanagementservice.dao.CollectCallbackDao;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.exceptions.CollectCallbackExistsException;
import com.kmbl.eventmanagementservice.model.CollectCallback;
import com.kmbl.eventmanagementservice.model.CollectCallbackEvent;
import com.kmbl.eventmanagementservice.service.requests.CreateCollectCallbackRequest;
import com.kmbl.eventmanagementservice.utils.EMSEventPublisher;
import com.kmbl.eventmanagementservice.utils.EMSMetricUtil;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import javax.annotation.concurrent.ThreadSafe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ThreadSafe
@Service
public class CollectCallbackService {
    private final CollectCallbackDao dao;
    private final EMSMetricUtil metricUtil;
    private final EpochProvider epochProvider;
    private final EMSEventPublisher emsEventPublisher;

    public CollectCallbackService(CollectCallbackDao dao, EMSMetricUtil metricUtil, EpochProvider epochProvider) {
        this.dao = dao;
        this.metricUtil = metricUtil;
        this.epochProvider = new EpochProvider();
        this.emsEventPublisher = new EMSEventPublisher();
    }

    public CollectCallback create(CreateCollectCallbackRequest request) {
        var currentTime = epochProvider.currentEpoch();
        var collectCallback = request.toCollectCallback(currentTime, EventName.COLLECT_CALLBACK_API);
        CollectCallbackEvent collectCallbackEvent =
                request.toCollectCallbackEvent(currentTime, EventName.COLLECT_CALLBACK_API);
        try (var ignored = metricUtil.timeIt("CollectCallbackService.create.Latency")) {
            dao.create(collectCallback);
        } catch (CollectCallbackExistsException e) {
            var existing = dao.getByTransactionIdAndType(request.transactionId(), request.type())
                    .orElseThrow(() -> e);
            log.info("there is already a event with transactionId" + request.transactionId() + " and type "
                    + request.type());
            log.info("new event is not published with transactionId" + request.transactionId() + " and type "
                    + request.type());
            return existing;
        }
        log.info("event is published with transactionId" + request.transactionId() + " and type " + request.type());
        emsEventPublisher.publish(collectCallbackEvent, "topic");
        return collectCallback;
    }
}
