package com.kotak.merchant.payments.gateway.service.unittest;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.dtos.Currency;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.dtos.TransactionAmount;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventName;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventStatus;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.PaymentStatus;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.model.CollectCallback;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;

import static com.kotak.merchant.payments.gateway.service.testutils.RandUtils.randInstant;
import static com.kotak.merchant.payments.gateway.service.testutils.RandUtils.randStr;

public class UnitDataGen {
    private static final String VPA_SUFFIX = "@kotak";
    public static final Long MAX_CREDIT_TRANSFER_LIMIT = 9999999999999L;

    public static CollectCallback randCredit(EventName eventName) {
        return CollectCallback.builder()
                .transactionId(randTransactionId())
                .aggregatorCode(randStr(6))
                .merchantCode(randStr(6))
                .status(PaymentStatus.SUCCESS.toString())
                .statusCode(randStr(6))
                .description(randStr(10))
                .remarks(randStr(10))
                .transactionReferenceNumber(randStr(12))
                .rrn(randStr(7))
                .amount(randAmount())
                .type(randStr(5))
                .payerVpa(randStr(10))
                .payeeVpa(randStr(10))
                .refUrl(randStr(20))
                .refId(randStr(8))
                .initMode(randStr(5))
                .transactionTimestamp(randInstant().toEpochMilli())
                .checksum(randStr(5))
                .accType(randStr(4))
                .cardType(randStr(4))
                .bin(randStr(6))
                .payerMobileNumber(randStr(10))
                .payerAccountNumber(randStr(12))
                .payerAccountName(randStr(12))
                .payerAccountIFSC(randStr(11))
                .creationTime(System.currentTimeMillis())
                .eventStatus(EventStatus.PENDING)
                .createdBy(eventName)
                .build();
    }

    public static String randTransactionId() {
        return randStr(10);
    }

    public static TransactionAmount randAmount() {
        var rupees = RandomUtils.nextLong(1, UnitDataGen.MAX_CREDIT_TRANSFER_LIMIT);
        return TransactionAmount.builder()
                .value(BigDecimal.valueOf(rupees))
                .currency(Currency.INR)
                .build();
        //    var paisa = RandomUtils.nextLong(0, 100);
        //    return new BigDecimal(String.format("%d.%d", rupees, paisa));
    }
}
