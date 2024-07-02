package com.kmbl.eventmanagementservice.service.dtos;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.enums.PaymentStatus;
import com.kmbl.eventmanagementservice.service.PartitionedEvent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record CollectCallbackEvent(
        @NotNull String transactionId,
        String aggregatorCode,
        String merchantCode,
        String status,
        String statusCode,
        String description,
        String remarks,
        String transactionReferenceNumber,
        String rrn,
        String amount,
        @NotNull String type,
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
        String payerAccountIFSC,
        EventName createdBy) implements PartitionedEvent {

    public CollectCallback toCollectCallback(EventStatus eventStatus)
    {
        return CollectCallback.builder()
                .transactionId(transactionId)
                .type(type)
                .metaData(getMetaData())
                .eventStatus(eventStatus)
                .createdBy(createdBy)
                .build();
    }

    private CollectCallbackData getMetaData() {
        return CollectCallbackData.builder()
                .transactionId(transactionId)
                .aggregatorCode(aggregatorCode)
                .merchantCode(merchantCode)
                .status(PaymentStatus.valueOf(status))
                .statusCode(statusCode)
                .description(description)
                .remarks(remarks)
                .transactionReferenceNumber(transactionReferenceNumber)
                .rrn(rrn)
                .amount(amount)
                .type(type)
                .payerVpa(payerVpa)
                .payeeVpa(payeeVpa)
                .refUrl(refUrl)
                .refId(refId)
                .initMode(initMode)
                .transactionTimestamp(transactionTimestamp)
                .checksum(checksum)
                .accType(accType)
                .cardType(cardType)
                .bin(bin)
                .payerMobileNumber(payerMobileNumber)
                .payerAccountNumber(payerAccountNumber)
                .payerAccountName(payerAccountName)
                .payerAccountName(payerAccountIFSC)
                .build();
    }

    @Override
    public String uniqueId() {
        return transactionId;
    }

    @Override
    public String partitionKey() {
        return transactionId;
    }
}
