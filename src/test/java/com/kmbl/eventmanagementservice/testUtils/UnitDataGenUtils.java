package com.kmbl.eventmanagementservice.testUtils;


import static com.kmbl.eventmanagementservice.testUtils.RandUtils.*;

import com.kmbl.eventmanagementservice.Schema.CBSTranCol;
import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.enums.PaymentStatus;
import com.kmbl.eventmanagementservice.model.CollectCallback;
import com.kmbl.eventmanagementservice.service.dtos.Currency;
import com.kmbl.eventmanagementservice.service.dtos.TransactionAmount;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

@Component
public class UnitDataGenUtils {

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
        var rupees = RandomUtils.nextLong(1, UnitDataGenUtils.MAX_CREDIT_TRANSFER_LIMIT);
        return TransactionAmount.builder()
                .value(BigDecimal.valueOf(rupees))
                .currency(Currency.INR)
                .build();
        //    var paisa = RandomUtils.nextLong(0, 100);
        //    return new BigDecimal(String.format("%d.%d", rupees, paisa));
    }

    private  static EpochProvider epochProvider;


    public static CBSTransactionLogs getCBSInsertDataEvent() {
        CBSTranCol before = null;
        CBSTranCol after = randCBSTranLogColumns();
        var event = new  CBSTransactionLogs();
        event.setAfter(after);
        event.setBefore(before);
        event.setOpType("I");

        event.setOpTs(formatDateTime(
                randEpoch(), OP_DATETIME_FORMAT, ZoneId.of(UTC_ZONE_ID)));
        event.setCurrentTs(formatDateTime(
                randEpoch(), OP_DATETIME_FORMAT, ZoneId.of(UTC_ZONE_ID)));
        event.setSourceScn("1");
        event.setPos("1");
        return event;
    }
    public static final String IST_ZONE_ID = "Asia/Kolkata";
    public static final String UTC_ZONE_ID = "UTC";
    public static final String OP_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String LCHG_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String CBS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter OP_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(OP_DATETIME_FORMAT);
    public static String formatDateTime(Long epochMillis, String format, ZoneId zoneId) {
        if (Objects.isNull(epochMillis)) {
            return null;
        }
        var zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), zoneId);
        var formatter = DateTimeFormatter.ofPattern(format);
        return zonedDateTime.format(formatter);
    }
    public static CBSTranCol randCBSTranLogColumns() {
        CBSTranCol tranCol = new CBSTranCol();
        tranCol.setTXNID(randStr(16));
        tranCol.setTRANTYPE("debit");
        tranCol.setAMOUNT("200");
        tranCol.setDEBITACCOUNT("1234");
        tranCol.setCREDITACCOUNT("1234");
        tranCol.setCBSRC("00");
        return tranCol;
    }
}