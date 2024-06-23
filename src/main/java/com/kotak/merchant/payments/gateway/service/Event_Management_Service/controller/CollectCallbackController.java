package com.kotak.merchant.payments.gateway.service.Event_Management_Service.controller;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.CollectCallbackService;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.requests.CreateCollectCallbackRequest;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EMSMetricUtil;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.CollectCallbackStatus;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.requests.ApiCreateCollectCallbackRequest;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.responses.ApiCreateCollectCallbackResponse;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ConditionalOnProperty(prefix = "app", name = "server-enabled", havingValue = "true", matchIfMissing = true)
@RestController
@Timed(percentiles = {0.0, 0.75, 0.9, 0.95, 0.99, 1.0})
public class CollectCallbackController {
    public static final String EP_CREATE_COLLECT_CALLBACK = "/collect-callback";
    public static final String EP_COLLECT_CALLBACK = "/collect-callback/{id}";

    private final CollectCallbackService collectCallbackService;
    private final EMSMetricUtil metricUtil;

    public CollectCallbackController(EMSMetricUtil metricUtil, CollectCallbackService collectCallbackService) {
        this.metricUtil = metricUtil;
        this.collectCallbackService = collectCallbackService;
    }

    @PostMapping(EP_CREATE_COLLECT_CALLBACK)
    @ResponseStatus(HttpStatus.CREATED)
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public ApiCreateCollectCallbackResponse createCollectCallback(
            @RequestBody ApiCreateCollectCallbackRequest request) {
        try (var ignored = metricUtil.timeIt("CreateCollectCallback.Latency")) {
            log.info("CreateTransactionRequest: {}", request);
            // validate(request);
            var createTransactionRequest = CreateCollectCallbackRequest.from(request);
            var collectCallback = collectCallbackService.create(createTransactionRequest);

            var response = ApiCreateCollectCallbackResponse.builder()
                    .collectCallbackStatus(CollectCallbackStatus.SUCCESS_CREATED_NOW)
                    .collectCallback(collectCallback)
                    .build();
            log.info("CreateCollectCallbackResponse: {}", response);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }
}
