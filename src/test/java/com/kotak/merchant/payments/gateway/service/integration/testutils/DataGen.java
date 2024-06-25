package com.kotak.merchant.payments.gateway.service.integration.testutils;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.dtos.Currency;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.dtos.TransactionAmount;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventName;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventStatus;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.PaymentStatus;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.model.CollectCallback;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.requests.ApiCreateCollectCallbackRequest;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

import static com.kotak.merchant.payments.gateway.service.testutils.RandUtils.randStr;
import static java.time.temporal.ChronoUnit.DAYS;

public class DataGen {

    public static BigDecimal randAmount() {
        return BigDecimal.valueOf(RandomUtils.nextDouble(0.01, 100000.00));
    }

    public static String randDateString() {
        LocalDate start = LocalDate.of(2000, 1, 1), end = LocalDate.of(2030, 12, 31);
        long daysBetween = DAYS.between(start, end);
        long randomDays = new Random().nextInt((int) daysBetween);
        LocalDate randomDate = start.plusDays(randomDays);
        return randomDate.toString();
    }

    public static CollectCallback apiCreateCollectCallbackRequestResponseCollectCallback(ApiCreateCollectCallbackRequest req) {
        return CollectCallback.builder()
                .transactionId(req.transactionid())
                .aggregatorCode(req.aggregatorcode())
                .merchantCode(req.merchantcode())
                .status(req.status() != null ? req.status().toString() : null)
                .statusCode(req.statusCode())
                .description(req.description())
                .remarks(req.remarks())
                .transactionReferenceNumber(req.transactionreferencenumber())
                .rrn(req.rrn())
                .amount(new TransactionAmount(new BigDecimal(req.amount()), Currency.INR)) // Assuming TransactionAmount needs to be constructed
                .type(req.type())
                .payerVpa(req.payervpa())
                .payeeVpa(req.payeevpa())
                .refUrl(req.refurl())
                .refId(req.refid())
                .initMode(req.initmode())
                .transactionTimestamp(processTransactionTimestamp(req.transactionTimestamp())) // Convert String to long
                .checksum(req.checksum())
                .accType(req.accType())
                .cardType(req.cardType())
                .bin(req.bin())
                .payerMobileNumber(req.payerMobileNumber())
                .payerAccountNumber(req.payerAccountNumber())
                .payerAccountName(req.payerAccountName())
                .payerAccountIFSC(req.payerAccountIFSC())
                .createdBy(EventName.COLLECT_CALLBACK_API)
                .eventStatus(EventStatus.DELIVERED)
                // You need to provide an instance of EventName here
                .build();
    }

    public static ApiCreateCollectCallbackRequest randApiCreateCollectCallback() {
        return ApiCreateCollectCallbackRequest.builder()
                .transactionid(randStr(12))
                .aggregatorcode(randStr(6))
                .merchantcode(randStr(6))
                .status(PaymentStatus.SUCCESS)
                .statusCode(randStr(6))
                .description(randStr(10))
                .remarks(randStr(10))
                .transactionreferencenumber(randStr(12))
                .rrn(randStr(7))
                .amount("100.0")
                .type(randStr(5))
                .payervpa(randStr(10))
                .payeevpa(randStr(10))
                .refurl(randStr(20))
                .refid(randStr(8))
                .initmode(randStr(5))
                .transactionTimestamp("")
                .checksum(randStr(5))
                .accType(randStr(4))
                .cardType(randStr(4))
                .bin(randStr(6))
                .payerMobileNumber(randStr(10))
                .payerAccountNumber(randStr(12))
                .payerAccountName(randStr(12))
                .payerAccountIFSC(randStr(11))
                .build();
    }

    private static Long processTransactionTimestamp(String str) {
        if (str == null || str.isBlank() || str.isEmpty()) {
            return 0L;
        }
        return Long.parseLong(str);
    }
}
