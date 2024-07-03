package com.kmbl.eventmanagementservice.integration.testutils;

import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randStr;
import static java.time.temporal.ChronoUnit.DAYS;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.enums.PaymentStatus;
import com.kmbl.eventmanagementservice.model.requests.ApiCreateCollectCallbackRequest;
import com.kmbl.eventmanagementservice.service.dtos.CollectCallback;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;
import org.apache.commons.lang3.RandomUtils;

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
                .type(req.type())
                .createdBy(EventName.GG_TRAN_LOG)
                .eventStatus(EventStatus.DELIVERED)
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
        return Instant.parse(str).toEpochMilli();
    }
}
