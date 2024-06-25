package com.kmbl.eventmanagementservice.service.requests;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.enums.PaymentStatus;
import com.kmbl.eventmanagementservice.model.CollectCallback;
import com.kmbl.eventmanagementservice.model.CollectCallbackEvent;
import com.kmbl.eventmanagementservice.requests.ApiCreateCollectCallbackRequest;
import com.kmbl.eventmanagementservice.service.dtos.Currency;
import com.kmbl.eventmanagementservice.service.dtos.TransactionAmount;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record CreateCollectCallbackRequest(
        String transactionId,
        String aggregatorCode,
        String merchantCode,
        PaymentStatus status,
        String statusCode,
        String description,
        String remarks,
        String transactionReferenceNumber,
        String rrn,
        String amount,
        String type,
        String payerVpa,
        String payeeVpa,
        String refUrl,
        String refId,
        String initMode,
        String transactionTimestamp,
        String checksum,
        String accType,
        String cardType,
        String bin,
        String payerMobileNumber,
        String payerAccountNumber,
        String payerAccountName,
        String payerAccountIfsc) {

    public CollectCallback toCollectCallback(long currentTime, EventName eventName) {
        BigDecimal amount = new BigDecimal(amount());
        TransactionAmount trm= new TransactionAmount(amount, Currency.INR);
        return CollectCallback.builder()
                .transactionId(transactionId())
                .aggregatorCode(aggregatorCode())
                .merchantCode(merchantCode())
                .status(String.valueOf(status()))
                .statusCode(statusCode())
                .description(description())
                .remarks(remarks())
                .transactionReferenceNumber(transactionReferenceNumber())
                .rrn(rrn())
                .amount(trm)
                .type(type()) // @NotNull field, assuming non-null in CreateCollectCallbackRequest
                .payerVpa(payerVpa())
                .payeeVpa(payeeVpa())
                .refUrl(refUrl())
                .refId(refId())
                .initMode(initMode())
                .transactionTimestamp(processTransactionTimestamp(transactionTimestamp()))
                .checksum(checksum())
                .accType(accType())
                .cardType(cardType())
                .bin(bin())
                .payerMobileNumber(payerMobileNumber())
                .payerAccountNumber(payerAccountNumber())
                .payerAccountName(payerAccountName())
                .payerAccountIFSC(payerAccountIfsc())
                .eventStatus(EventStatus.DELIVERED)
                .creationTime(currentTime)
                .createdBy(eventName)
                .build();
    }

    public CollectCallbackEvent toCollectCallbackEvent(long currentTime, EventName eventName) {
        return CollectCallbackEvent.builder()
                .transactionId(transactionId())
                .aggregatorCode(aggregatorCode())
                .merchantCode(merchantCode())
                .status(String.valueOf(status()))
                .statusCode(statusCode())
                .description(description())
                .remarks(remarks())
                .transactionReferenceNumber(transactionReferenceNumber())
                .rrn(rrn())
                .amount(amount())
                .type(type()) // @NotNull field, assuming non-null in CreateCollectCallbackRequest
                .payerVpa(payerVpa())
                .payeeVpa(payeeVpa())
                .refUrl(refUrl())
                .refId(refId())
                .initMode(initMode())
                .transactionTimestamp(processTransactionTimestamp(transactionTimestamp()))
                .checksum(checksum())
                .accType(accType())
                .cardType(cardType())
                .bin(bin())
                .payerMobileNumber(payerMobileNumber())
                .payerAccountNumber(payerAccountNumber())
                .payerAccountName(payerAccountName())
                .payerAccountIFSC(payerAccountIfsc())
                .creationTime(currentTime)
                .createdBy(eventName)
                .build();
    }

    public static CreateCollectCallbackRequest from(ApiCreateCollectCallbackRequest apiRequest) {
        return CreateCollectCallbackRequest.builder()
                .transactionId(apiRequest.transactionid())
                .aggregatorCode(apiRequest.aggregatorcode())
                .merchantCode(apiRequest.merchantcode())
                .status(apiRequest.status())
                .statusCode(apiRequest.statusCode())
                .description(apiRequest.description())
                .remarks(apiRequest.remarks())
                .transactionReferenceNumber(apiRequest.transactionreferencenumber())
                .rrn(apiRequest.rrn())
                .amount(apiRequest.amount())
                .type(apiRequest.type())
                .payerVpa(apiRequest.payervpa())
                .payeeVpa(apiRequest.payeevpa())
                .refUrl(apiRequest.refurl())
                .refId(apiRequest.refid())
                .initMode(apiRequest.initmode())
                .transactionTimestamp(apiRequest.transactionTimestamp())
                .checksum(apiRequest.checksum())
                .accType(apiRequest.accType())
                .cardType(apiRequest.cardType())
                .bin(apiRequest.bin())
                .payerMobileNumber(apiRequest.payerMobileNumber())
                .payerAccountNumber(apiRequest.payerAccountNumber())
                .payerAccountName(apiRequest.payerAccountName())
                .payerAccountIfsc(apiRequest.payerAccountIFSC())
                .build();
    }

    private static Long processTransactionTimestamp(String str) {
        if (str == null || str.isBlank() || str.isEmpty()) {
            return 0L;
        }
        return Long.parseLong(str);
    }
}
