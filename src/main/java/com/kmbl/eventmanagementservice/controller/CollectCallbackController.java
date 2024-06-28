package com.kmbl.eventmanagementservice.controller;

import com.kmbl.eventmanagementservice.enums.CollectCallbackStatus;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.requests.ApiCreateCollectCallbackRequest;
import com.kmbl.eventmanagementservice.responses.ApiCreateCollectCallbackResponse;
import com.kmbl.eventmanagementservice.service.CBSTranLogGGService;
import com.kmbl.eventmanagementservice.service.CollectCallbackService;
import com.kmbl.eventmanagementservice.service.requests.CBSTranLogGGRequest;
import com.kmbl.eventmanagementservice.service.requests.CreateCollectCallbackRequest;
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
    private final CBSTranLogGGService cbsTranLogGGService;

    public CollectCallbackController( CollectCallbackService collectCallbackService, CBSTranLogGGService cbsTranLogGGService) {
        this.collectCallbackService = collectCallbackService;
        this.cbsTranLogGGService = cbsTranLogGGService;
    }

    @PostMapping(EP_CREATE_COLLECT_CALLBACK)
    @ResponseStatus(HttpStatus.CREATED)
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public ApiCreateCollectCallbackResponse createCollectCallback(
            @RequestBody ApiCreateCollectCallbackRequest request) {
        try  {
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

    @PostMapping("/fun")
    @ResponseStatus(HttpStatus.CREATED)
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void fun() {
        var req = CBSTranLogGGRequest.builder()
                .transactionId("123")
                .type("321")
                .eventName(EventName.GG_CBS_TRAN_LOG)
                .build();
        cbsTranLogGGService.create(req);
    }
}
