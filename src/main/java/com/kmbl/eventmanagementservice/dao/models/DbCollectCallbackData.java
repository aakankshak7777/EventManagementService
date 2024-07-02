package com.kmbl.eventmanagementservice.dao.models;

import com.kmbl.eventmanagementservice.enums.PaymentStatus;
import com.kmbl.eventmanagementservice.service.dtos.CollectCallbackData;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbCollectCallbackData {


    private String transactionId;

    private String aggregatorCode;

    private String merchantCode;

    private PaymentStatus status;

    private String statusCode;

    private String description;

    private String remarks;

    private String transactionReferenceNumber;

    private String rrn;

    private String amount;

    private String type;

    private String payerVpa;

    private String payeeVpa;

    private String refUrl;

    private String refId;

    private String initMode;

    private String transactionTimestamp;

    private String checksum;

    private String accType;

    private String cardType;

    private String bin;

    private String payerMobileNumber;

    private String payerAccountNumber;

    private String payerAccountName;

    private String payerAccountIfsc;

    public CollectCallbackData to() {
        return CollectCallbackData.builder()
                .transactionId(transactionId)
                .aggregatorCode(aggregatorCode)
                .merchantCode(merchantCode)
                .status(status)
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
                .payerAccountIfsc(payerAccountIfsc)
                .build();
    }

    public static DbCollectCallbackData from(CollectCallbackData collectCallbackData) {
        if (collectCallbackData == null) {
            return null;
        }
        return DbCollectCallbackData.builder()
                .transactionId(collectCallbackData.transactionId())
                .aggregatorCode(collectCallbackData.aggregatorCode())
                .merchantCode(collectCallbackData.merchantCode())
                .status(collectCallbackData.status())
                .statusCode(collectCallbackData.statusCode())
                .description(collectCallbackData.description())
                .remarks(collectCallbackData.remarks())
                .transactionReferenceNumber(collectCallbackData.transactionReferenceNumber())
                .rrn(collectCallbackData.rrn())
                .amount(collectCallbackData.amount())
                .type(collectCallbackData.type())
                .payerVpa(collectCallbackData.payerVpa())
                .payeeVpa(collectCallbackData.payeeVpa())
                .refUrl(collectCallbackData.refUrl())
                .refId(collectCallbackData.refId())
                .initMode(collectCallbackData.initMode())
                .transactionTimestamp(collectCallbackData.transactionTimestamp())
                .checksum(collectCallbackData.checksum())
                .accType(collectCallbackData.accType())
                .cardType(collectCallbackData.cardType())
                .bin(collectCallbackData.bin())
                .payerMobileNumber(collectCallbackData.payerMobileNumber())
                .payerAccountNumber(collectCallbackData.payerAccountNumber())
                .payerAccountName(collectCallbackData.payerAccountName())
                .payerAccountIfsc(collectCallbackData.payerAccountIfsc())
                .build();
    }
}
