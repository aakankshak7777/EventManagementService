package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EMSMetricUtil;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EpochProvider;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.dao.CollectCallbackDao;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventName;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventStatus;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.exceptions.CollectCallbackExistsException;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.model.CollectCallback;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.requests.CreateCollectCallbackRequest;
import javax.annotation.concurrent.ThreadSafe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ThreadSafe
@RequiredArgsConstructor
public class CollectCallbackService {
    private final CollectCallbackDao dao;
    private final EMSMetricUtil metricUtil;
    private final EpochProvider epochProvider;

    public CollectCallback create(CreateCollectCallbackRequest request) {
        var currentEpoch = epochProvider.currentEpoch();
        var collectCallback = request.toCollectCallback().toBuilder()
                .creationTime(currentEpoch)
                .createdBy(EventName.COLLECT_CALLBACK_API)
                .eventStatus(EventStatus.PENDING)
                .build();
        try (var ignored = metricUtil.timeIt("CollectCallbackService.create.Latency")) {
            dao.create(collectCallback);
        } catch (CollectCallbackExistsException e) {
            var existing = dao.getByTransactionIdAndType(request.transactionId(), request.type())
                    .orElseThrow(() -> e);
            return existing;
        }
        return collectCallback;
    }
}
