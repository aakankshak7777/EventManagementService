package com.kmbl.eventmanagementservice.model;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.service.PartitionedEvent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
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
        Long creationTime,
        EventName createdBy) implements PartitionedEvent {

    @Override
    public String uniqueId() {
        return transactionId;
    }

    @Override
    public String partitionKey() {
        return transactionId;
    }
}
