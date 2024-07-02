package com.kmbl.eventmanagementservice.service.dtos.requests;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.model.requests.ApiCreateCollectCallbackRequest;
import com.kmbl.eventmanagementservice.service.dtos.CollectCallback;
import com.kmbl.eventmanagementservice.service.dtos.CollectCallbackData;
import com.kmbl.eventmanagementservice.service.dtos.CollectCallbackEvent;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record CollectCallbackRequest(
        @NotNull String transactionId,
        @NotNull String type,
        CollectCallbackData metaData,
        EventName eventName) {

    public CollectCallback toCollectCallbackRequest( EventName eventName, EventStatus eventStatus) {
        return CollectCallback.builder()
                .transactionId(transactionId())
                .type(type())
                .metaData(buildMetaData())
                .eventStatus(eventStatus)
                .createdBy(eventName)
                .build();
    }

    public CollectCallbackEvent toCollectCallbackEvent(EventName eventName) {
        var metaData = metaData();
        if (metaData == null) {
            return null;
        }
        return CollectCallbackEvent.builder()
                .transactionId(transactionId())
                .aggregatorCode(metaData.aggregatorCode())
                .merchantCode(metaData.merchantCode())
                .status(metaData.status().toString())
                .statusCode(metaData.statusCode())
                .description(metaData.description())
                .remarks(metaData.remarks())
                .transactionReferenceNumber(metaData.transactionReferenceNumber())
                .rrn(metaData.rrn())
                .amount(metaData.amount())
                .type(metaData.type()) // @NotNull field, assuming non-null in CreateCollectCallbackRequest
                .payerVpa(metaData.payerVpa())
                .payeeVpa(metaData.payeeVpa())
                .refUrl(metaData.refUrl())
                .refId(metaData.refId())
                .initMode(metaData.initMode())
                .transactionTimestamp(metaData.transactionTimestamp())
                .checksum(metaData.checksum())
                .accType(metaData.accType())
                .cardType(metaData.cardType())
                .bin(metaData.bin())
                .payerMobileNumber(metaData.payerMobileNumber())
                .payerAccountNumber(metaData.payerAccountNumber())
                .payerAccountName(metaData.payerAccountName())
                .payerAccountIFSC(metaData.payerAccountIfsc())
                .createdBy(eventName)
                .build();
    }

    public static CollectCallbackRequest from(ApiCreateCollectCallbackRequest apiRequest) {
        return CollectCallbackRequest.builder()
                .transactionId(apiRequest.transactionid())
                .type(apiRequest.type())
                .eventName(EventName.COLLECT_CALLBACK_API)
                .metaData(getMetaData(apiRequest))
                .build();
    }

    private static CollectCallbackData getMetaData(ApiCreateCollectCallbackRequest apiRequest) {
        return CollectCallbackData.builder()
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

    private CollectCallbackData buildMetaData() {
        var metaData = metaData();
        if (metaData == null) {
            return null;
        }
        return CollectCallbackData.builder()
                .transactionId(transactionId())
                .aggregatorCode(metaData.aggregatorCode())
                .merchantCode(metaData.merchantCode())
                .status(metaData.status())
                .statusCode(metaData.statusCode())
                .description(metaData.description())
                .remarks(metaData.remarks())
                .transactionReferenceNumber(metaData.transactionReferenceNumber())
                .rrn(metaData.rrn())
                .amount(metaData.amount())
                .type(type()) // @NotNull field, assuming non-null in CreateCollectCallbackRequest
                .payerVpa(metaData.payerVpa())
                .payeeVpa(metaData.payeeVpa())
                .refUrl(metaData.refUrl())
                .refId(metaData.refId())
                .initMode(metaData.initMode())
                .transactionTimestamp(metaData.transactionTimestamp())
                .checksum(metaData.checksum())
                .accType(metaData.accType())
                .cardType(metaData.cardType())
                .bin(metaData.bin())
                .payerMobileNumber(metaData.payerMobileNumber())
                .payerAccountNumber(metaData.payerAccountNumber())
                .payerAccountName(metaData.payerAccountName())
                .payerAccountIfsc(metaData.payerAccountIfsc())
                .build();
    }

    private static Long processTransactionTimestamp(String str) {
        if (str == null || str.isBlank() || str.isEmpty()) {
            return 0L;
        }
        return Instant.parse(str).toEpochMilli();
    }
}
