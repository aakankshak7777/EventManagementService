package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.requests.CreateCollectCallbackRequest;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EMSEventPublisher;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EMSMetricUtil;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EpochProvider;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.repository.dao.CollectCallbackDao;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventName;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.exceptions.CollectCallbackExistsException;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.model.CollectCallback;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.model.CollectCallbackEvent;
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
        this.epochProvider = epochProvider;
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
