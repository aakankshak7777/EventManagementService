package com.kotak.merchant.payments.gateway.service.Event_Management_Service.requests;

public record ApiCreateCollectCallbackRequest(
        String transactionid,
        String aggregatorcode,
        String merchantcode,
        String status,
        String statusCode,
        String description,
        String remarks,
        String transactionreferencenumber,
        String rrn,
        String amount,
        String type,
        String payervpa,
        String payeevpa,
        String refurl,
        String refid,
        String initmode,
        String transactionTimestamp,
        String checksum,
        String accType,
        String cardType,
        String bin,
        String payerMobileNumber,
        String payerAccountNumber,
        String payerAccountName,
        String payerAccountIFSC) {
}
