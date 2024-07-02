package com.kmbl.eventmanagementservice.service.dtos;

import com.kmbl.eventmanagementservice.dao.models.DbCollectCallbackData;
import com.kmbl.eventmanagementservice.enums.PaymentStatus;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record CollectCallbackData(
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

    public DbCollectCallbackData to() {
        return DbCollectCallbackData.builder()
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
                .type(type())
                .payerVpa(payerVpa())
                .payeeVpa(payeeVpa())
                .refUrl(refUrl())
                .refId(refId())
                .initMode(initMode())
                .transactionTimestamp(transactionTimestamp())
                .checksum(checksum())
                .accType(accType())
                .cardType(cardType())
                .bin(bin())
                .payerMobileNumber(payerMobileNumber())
                .payerAccountNumber(payerAccountNumber())
                .payerAccountName(payerAccountName())
                .payerAccountIfsc(payerAccountIfsc())
                .build();
    }
}
