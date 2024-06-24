package com.kotak.merchant.payments.gateway.service.Event_Management_Service.repository.dao.models;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventName;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventStatus;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.PaymentStatus;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.model.CollectCallback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbCollectCallback {

    public static final String TABLE_NAME = "CollectCallback";
    public static final String ATTR_TRANSACTION_ID = "transactionId";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_VERSION = "version";

    private String transactionId;
    private String aggregatorCode;
    private String merchantCode;
    private String status;
    private String statusCode;
    private String description;
    private String remarks;
    private String transactionReferenceNumber;
    private String rrn;
    private DbTransactionAmount amount;
    private String type;
    private String payerVpa;
    private String payeeVpa;
    private String refUrl;
    private String refId;
    private String initMode;
    private long transactionTimestamp;
    private String checksum;
    private String accType;
    private String cardType;
    private String bin;
    private String payerMobileNumber;
    private String payerAccountNumber;
    private String payerAccountName;
    private String payerAccountIFSC;
    private Long creationTime;
    private EventStatus eventStatus;
    private EventName createdBy;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(ATTR_TRANSACTION_ID)
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute(ATTR_TYPE)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Convert this object to a Transaction DTO.
     */
    public CollectCallback to() {
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
                .amount(amount.to())
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
                .creationTime(creationTime)
                .build();
    }

    /**
     * Convert a CollectCallback DTO to DbCollectCallback.
     */
    public static DbCollectCallback from(CollectCallback collectCallback) {
        DbCollectCallback callback = new DbCollectCallback();
        callback.setTransactionId(collectCallback.transactionId());
        callback.setAggregatorCode(collectCallback.aggregatorCode());
        callback.setMerchantCode(collectCallback.merchantCode());
        callback.setStatus(collectCallback.status().toString());
        callback.setStatusCode(collectCallback.statusCode());
        callback.setDescription(collectCallback.description());
        callback.setRemarks(collectCallback.remarks());
        callback.setTransactionReferenceNumber(collectCallback.transactionReferenceNumber());
        callback.setRrn(collectCallback.rrn());
        callback.setAmount(DbTransactionAmount.from(collectCallback.amount()));
        callback.setType(collectCallback.type());
        callback.setPayerVpa(collectCallback.payerVpa());
        callback.setPayeeVpa(collectCallback.payeeVpa());
        callback.setRefUrl(collectCallback.refUrl());
        callback.setRefId(collectCallback.refId());
        callback.setInitMode(collectCallback.initMode());
        callback.setTransactionTimestamp(collectCallback.transactionTimestamp());
        callback.setChecksum(collectCallback.checksum());
        callback.setAccType(collectCallback.accType());
        callback.setCardType(collectCallback.cardType());
        callback.setBin(collectCallback.bin());
        callback.setPayerMobileNumber(collectCallback.payerMobileNumber());
        callback.setPayerAccountNumber(collectCallback.payerAccountNumber());
        callback.setPayerAccountName(collectCallback.payerAccountName());
        callback.setPayerAccountIFSC(collectCallback.payerAccountIFSC());
        callback.setEventStatus(collectCallback.eventStatus());
        callback.setCreationTime(collectCallback.creationTime());
        callback.setCreatedBy(collectCallback.createdBy());
        return callback;
    }
}
