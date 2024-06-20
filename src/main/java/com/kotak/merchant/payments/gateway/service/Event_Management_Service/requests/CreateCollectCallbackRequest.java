package com.kotak.merchant.payments.gateway.service.Event_Management_Service.requests;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.model.CollectCallback;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record CreateCollectCallbackRequest(
        String transactionId,
        String aggregatorCode,
        String merchantCode,
        String status,
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

    public CollectCallback toCollectCallback() {
        return CollectCallback.builder()
                .transactionId(transactionId())
                .aggregatorCode(aggregatorCode())
                .merchantCode(merchantCode())
                .status(status())
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
                .transactionTimestamp(Long.parseLong(transactionTimestamp()))
                .checksum(checksum())
                .accType(accType())
                .cardType(cardType())
                .bin(bin())
                .payerMobileNumber(payerMobileNumber())
                .payerAccountNumber(payerAccountNumber())
                .payerAccountName(payerAccountName())
                .payerAccountIFSC(payerAccountIfsc())
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
}
