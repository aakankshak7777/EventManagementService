package com.kmbl.eventmanagementservice;

import static com.kmbl.eventmanagementservice.testutils.RandUtils.randInstant;
import static com.kmbl.eventmanagementservice.testutils.RandUtils.randStr;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.enums.PaymentStatus;
import com.kmbl.eventmanagementservice.model.CollectCallback;
import com.kmbl.eventmanagementservice.service.dtos.Currency;
import com.kmbl.eventmanagementservice.service.dtos.TransactionAmount;
import java.math.BigDecimal;
import org.apache.commons.lang3.RandomUtils;

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
