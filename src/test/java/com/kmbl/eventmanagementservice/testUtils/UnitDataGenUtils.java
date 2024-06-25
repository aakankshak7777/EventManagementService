package com.kmbl.eventmanagementservice.testUtils;


import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randEpoch;
import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randStr;

import com.kmbl.eventmanagementservice.Schema.CBSTranCol;
import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UnitDataGenUtils {

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