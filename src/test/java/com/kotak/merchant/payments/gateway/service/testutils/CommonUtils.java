package com.kotak.merchant.payments.gateway.service.testutils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CommonUtils {

    public static ByteBuffer stringToBB(String val) {
        if (val == null) {
            return null;
        }
        Charset charset = Charset.forName("UTF-8");
        return charset.encode(val);
    }

    public static <T> T last(List<T> lst) {
        return last(lst.stream());
    }

    public static <T> T last(Stream<T> stream) {
        return stream.reduce((first, second) -> second).get();
    }

    public static String formatDateTime(Long epochMillis, String format, ZoneId zoneId) {
        if (Objects.isNull(epochMillis)) {
            return null;
        }
        var zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), zoneId);
        var formatter = DateTimeFormatter.ofPattern(format);
        return zonedDateTime.format(formatter);
    }
}
