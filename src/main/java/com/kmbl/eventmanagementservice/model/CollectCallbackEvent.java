package com.kmbl.eventmanagementservice.model;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.service.PartitionedEvent;
import com.kmbl.eventmanagementservice.service.dtos.Currency;
import com.kmbl.eventmanagementservice.service.dtos.TransactionAmount;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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
        long transactionTimestamp,
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
        BigDecimal amount = new BigDecimal(amount());
        TransactionAmount trm= new TransactionAmount(amount, Currency.INR);
        return CollectCallback.builder()
                .transactionId(transactionId)
                .aggregatorCode(aggregatorCode)
                .merchantCode(merchantCode)
                .status(status)
                .statusCode(statusCode)
                .description(description)
                .remarks(remarks)
                .transactionReferenceNumber(transactionReferenceNumber)
                .rrn(rrn)
                .amount(trm)
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
                .payerAccountIFSC(payerAccountIFSC)
                .createdBy(createdBy)
                .eventStatus(eventStatus)
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
