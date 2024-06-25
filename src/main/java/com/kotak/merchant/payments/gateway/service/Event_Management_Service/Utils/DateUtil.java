package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtil {
    public static ZonedDateTime getZoneDate(Instant dt, String zoneId) {
        return dt.atZone(ZoneId.of(zoneId));
    }

    public static String convertEpochToDateTimeAmPm(long epoch) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        return sdf.format(new Date(epoch));
    }
}
