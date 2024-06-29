package com.kmbl.eventmanagementservice.controller;

import com.kmbl.eventmanagementservice.enums.CollectCallbackStatus;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.requests.ApiCreateCollectCallbackRequest;
import com.kmbl.eventmanagementservice.responses.ApiCreateCollectCallbackResponse;
import com.kmbl.eventmanagementservice.service.CBSTranLogGGService;
import com.kmbl.eventmanagementservice.service.CollectCallbackService;
import com.kmbl.eventmanagementservice.service.requests.CBSTranLogGGRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ConditionalOnProperty(prefix = "app", name = "server-enabled", havingValue = "true", matchIfMissing = true)
@RestController
public class CollectCallbackController {
    public static final String EP_CREATE_COLLECT_CALLBACK = "/collect-callback";
    public static final String EP_COLLECT_CALLBACK = "/collect-callback/{id}";

    private final CollectCallbackService collectCallbackService;
    private final CBSTranLogGGService cbsTranlogService;

    public CollectCallbackController( CollectCallbackService collectCallbackService, CBSTranLogGGService cbsTranLogGGService) {
        this.collectCallbackService = collectCallbackService;
        this. cbsTranlogService = cbsTranLogGGService;

    }

    @PostMapping(EP_CREATE_COLLECT_CALLBACK)

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public ResponseEntity<ApiCreateCollectCallbackResponse> processCollectCallbackEvent(
            @RequestBody ApiCreateCollectCallbackRequest request) {
        try  {
            log.info("Collect Callback request Received: {}", request);
            //ToDo write code for validations.
            // validate(request);
            var CollectCallbackRequest = com.kmbl.eventmanagementservice.service.requests.CollectCallbackRequest.from(request);
            collectCallbackService.processCallbackEvent(CollectCallbackRequest);
            var response = ApiCreateCollectCallbackResponse.builder()
                    .collectCallbackStatus(CollectCallbackStatus.SUCCESS_CREATED_NOW)
                    .message("Event is created successfully.")
                    .build();
            log.info("CreateCollectCallbackResponse: {}", response);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            var response = ApiCreateCollectCallbackResponse.builder()
                    .collectCallbackStatus(CollectCallbackStatus.FAILURE)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
        cbsTranlogService.create(req);
    }
}
