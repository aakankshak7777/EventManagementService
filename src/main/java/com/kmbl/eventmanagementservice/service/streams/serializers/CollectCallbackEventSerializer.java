package com.kmbl.eventmanagementservice.service.streams.serializers;

import com.kmbl.eventmanagementservice.Schema.CollectCallbackEventAvro;
import com.kmbl.eventmanagementservice.model.CollectCallbackEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;

import static java.util.Objects.isNull;

public class CollectCallbackEventSerializer implements Serializer<CollectCallbackEvent> {
    @Override
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public byte[] serialize(String topic, CollectCallbackEvent data) {
        try {
            CollectCallbackEventAvro collectCallBackEventAvro = toAvro(data);
            ByteBuffer byteBuffer = collectCallBackEventAvro.toByteBuffer(); // Use toByteBuffer()
            return byteBuffer.array();

        } catch (IOException e) {
            throw new SerializationException("Serialization failed for: " + data, e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, CollectCallbackEvent data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    private CollectCallbackEventAvro toAvro(CollectCallbackEvent data) {
        CollectCallbackEventAvro avroRecord = new CollectCallbackEventAvro();

        avroRecord.setTransactionId(data.transactionId());
        avroRecord.setAggregatorCode(data.aggregatorCode());
        avroRecord.setMerchantCode(data.merchantCode());
        avroRecord.setStatus(data.status());
        avroRecord.setStatusCode(data.statusCode());
        avroRecord.setDescription(data.description());
        avroRecord.setRemarks(data.remarks());
        avroRecord.setTransactionReferenceNumber(data.transactionReferenceNumber());
        avroRecord.setRrn(data.rrn());
        avroRecord.setAmount(data.amount());
        avroRecord.setType(data.type());
        avroRecord.setPayerVpa(data.payerVpa());
        avroRecord.setPayeeVpa(data.payeeVpa());
        avroRecord.setRefUrl(data.refUrl());
        avroRecord.setRefId(data.refId());
        avroRecord.setInitMode(data.initMode());
        avroRecord.setTransactionTimestamp(data.transactionTimestamp());
        avroRecord.setChecksum(data.checksum());
        avroRecord.setAccType(data.accType());
        avroRecord.setCardType(data.cardType());
        avroRecord.setBin(data.bin());
        avroRecord.setPayerMobileNumber(data.payerMobileNumber());
        avroRecord.setPayerAccountNumber(data.payerAccountNumber());
        avroRecord.setPayerAccountName(data.payerAccountName());
        avroRecord.setPayerAccountIFSC(data.payerAccountIFSC());
        avroRecord.setCreationTime(data.creationTime());
        avroRecord.setCreatedBy(data.createdBy().toString());

        return avroRecord;
    }
}