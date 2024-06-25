package com.kotak.merchant.payments.gateway.service.Event_Management_Service.model;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.dtos.TransactionAmount;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventName;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record CollectCallback(
        @NotNull String transactionId,
        String aggregatorCode,
        String merchantCode,
        String status,
        String statusCode,
        String description,
        String remarks,
        String transactionReferenceNumber,
        String rrn,
        TransactionAmount amount,
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
        EventStatus eventStatus,
        EventName createdBy) {}
