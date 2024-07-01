package com.kmbl.eventmanagementservice.controller;

import com.kmbl.eventmanagementservice.enums.CollectCallbackStatus;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.model.requests.ApiCreateCollectCallbackRequest;
import com.kmbl.eventmanagementservice.model.responses.ApiCreateCollectCallbackResponse;
import com.kmbl.eventmanagementservice.service.CBSTranLogService;
import com.kmbl.eventmanagementservice.service.CollectCallbackService;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogData;
import com.kmbl.eventmanagementservice.service.dtos.requests.CBSTranLogRequest;
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
public class EventManagementController {
    public static final String EP_CREATE_COLLECT_CALLBACK = "/collect-callback";

    private final CollectCallbackService collectCallbackService;
    private final CBSTranLogService cbsTranlogService;

    public EventManagementController(CollectCallbackService collectCallbackService, CBSTranLogService cbsTranLogService) {
        this.collectCallbackService = collectCallbackService;
        this. cbsTranlogService = cbsTranLogService;
    }

    @PostMapping(EP_CREATE_COLLECT_CALLBACK)

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public ResponseEntity<ApiCreateCollectCallbackResponse> processCollectCallbackEvent(
            @RequestBody ApiCreateCollectCallbackRequest request) {
        try  {
            log.info("Collect Callback request Received: {}", request);
            //ToDo write code for validations.
            // validate(request);
            var CollectCallbackRequest = com.kmbl.eventmanagementservice.service.dtos.requests.CollectCallbackRequest.from(request);
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
        var req = createSample();
        cbsTranlogService.processCallbackEvent(req);
    }

    public CBSTranLogRequest createSample() {
        CBSTranLogData metaData = createSample1(); // Using the previously defined sample data creation

        return new CBSTranLogRequest(
                "123456789",            // transactionId
                "Payment",              // type
                metaData,               // metaData using previously created sample
                EventName.GG_CBS_TRAN_LOG      // Example eventName, replace with actual value
        );
    }

    public CBSTranLogData createSample1() {
        return new CBSTranLogData(
                1.0,                        // id
                "100.00",                   // amount
                "CBSRC123",                 // cbsrc
                "CreditAcc123",             // creditAccount
                "Customer456",              // customerId
                "DebitAcc789",              // debitAccount
                "MSG123",                   // msgId
                123456L,                    // reversal
                "Completed",                // reversalStatus
                "RRN123",                   // rrn
                "STAN456",                  // stan
                "2023-06-28",               // txnDate
                "TXN789",                   // txnId
                "Payment",                  // type
                "UP123",                    // upirc
                "ORGTXN456",                // orgTxnId
                "Transaction successful",   // remarks
                "CustAcc789",               // customerAccount
                "John Doe",                 // name
                "payee@example.com",        // payeeVpa
                "payer@example.com",        // payerVpa
                "123",                      // seqNo
                "10.00",                    // commissionAmount
                "Ref123",                   // customerRefId
                "Transfer",                 // tranType
                "CreditBank",               // creditBankName
                "DebitBank",                // debitBankName
                "OtherAcc456",              // othersAccount
                "Web",                      // channel
                "App123",                   // appId
                "Yes",                      // preApproved
                "Beneficiary",              // beneficiaryName
                "9876543210",               // otherMobileNo
                "Jane Doe",                 // remitterName
                "Appr789",                  // approvalNum
                "MCC123",                   // mccCode
                "192.168.0.1",              // serverIp
                "2023-06-28",               // cbsRespDate
                "Auto",                     // initMode
                "General",                  // purposeCode
                "Savings",                  // creditAccountType
                "CREDIT123",                // creditIfsc
                "Savings",                  // debitAccountType
                "DEBIT789",                 // debitIfsc
                "Order789",                 // orderId
                "Payee456",                 // payeeCode
                "Payer789",                 // payerCode
                "Online",                   // subType
                "9876543210",               // remittorMobileNumber
                "IMEI123",                  // payeeImei
                "IMEI456",                  // payerImei
                "2023-06-28",               // gmtDate
                "Savings",                  // payeeAccountType
                "Checking",                 // payerAccountType
                "Consent1",                 // payerConsentName
                "TypeA",                    // payerConsentType
                "Value123",                 // payerConsentValue
                "ADJ789",                   // adjCode
                "Y",                        // adjFlag
                "2023-06-28",               // createDate
                "Narration103",             // creditAcctNarration103
                "DE46DATA",                 // de46
                "Narration102",             // debitAcctNarration102
                "18%",                      // gst
                "Loan789",                  // loanNumber
                "12:00 PM",                 // localTime
                "Narration125",             // narration125
                "5.00",                     // p2pTxnCharges
                "Scheme789",                // schemeCode
                "500.00",                   // txnAmount
                "Batch789",                 // batchId
                "200.00",                   // baseAmount
                "USD",                      // baseCurr
                "CON123",                   // conCode
                "1.20",                     // fx
                "Markup123",                // mkup
                "Addl4",                    // additional4
                "D12DATA",                  // d12
                "Addl1",                    // additional1
                "Addl2",                    // additional2
                "Addl3",                    // additional3
                "Addl5",                    // additional5
                "Routing123"                // cbsRouting
        );
    }
}
