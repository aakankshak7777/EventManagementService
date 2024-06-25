package com.kmbl.eventmanagementservice.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

public class EventsUtil {

    public static final String IST_ZONE_ID = "Asia/Kolkata";
    public static final String UTC_ZONE_ID = "UTC";

    public static final String OP_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    public static final String LCHG_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter OP_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(OP_DATETIME_FORMAT);

    public static long getEpochFromISTTimestamp(String timeStampStr, String dateTimeFormat) {
        return getEpoch(timeStampStr, dateTimeFormat, IST_ZONE_ID);
    }

    public static long getEpochFromUtcTimestamp(String timeStampStr, String dateTimeFormat) {
        var epoch = getEpoch(timeStampStr, dateTimeFormat, UTC_ZONE_ID);
        return epoch;
    }

    private static long getEpoch(String timeStampStr, String dateTimeFormat, String zoneId) {
        LocalDateTime localDateTime = LocalDateTime.parse(timeStampStr, DateTimeFormatter.ofPattern(dateTimeFormat));
        var instant = localDateTime.atZone(ZoneId.of(zoneId)).toInstant();
        return instant.toEpochMilli();
    }

    public static String byteBuffToStr(ByteBuffer value) {
        return value == null
                ? null
                : StandardCharsets.UTF_8.decode(value.duplicate()).toString();
    }

    public static <T, V> Function<T, Optional<Instant>> generateTimestampFunc(
            Function<T, V> beforeFunc,
            Function<T, V> afterFunc,
            Function<V, CharSequence> eventTimestampFunc,
            Function<T, CharSequence> opTsFunc) {
        return r -> {
            Long epoch = null;
            var afterTs = getTs(afterFunc, eventTimestampFunc, r);
            if (afterTs != null) {
                var beforeTs = getTs(beforeFunc, eventTimestampFunc, r);
                // We've seen cases where LCHG time is same in both before and after
                // So we fall back to op_ts in case they are the same
                // https://dev.azure.com/kmbl-devops/Payments/_workitems/edit/196293
                if (!afterTs.equals(beforeTs)) {
                    epoch = getEpochFromISTTimestamp(afterTs.toString(), LCHG_DATETIME_FORMAT);
                }
            }
            if (epoch == null) {
                var opTs = opTsFunc.apply(r);
                if (opTs != null) {
                    epoch = LocalDateTime.parse(opTs, OP_DATETIME_FORMATTER)
                            .atOffset(ZoneOffset.UTC)
                            .toInstant()
                            .toEpochMilli();
                }
            }
            return Optional.ofNullable(epoch).map(Instant::ofEpochMilli);
        };
    }

    private static <T, V> CharSequence getTs(Function<T, V> vfunc, Function<V, CharSequence> tsFunc, T msg) {
        return Optional.ofNullable(vfunc.apply(msg)).map(tsFunc).orElse(null);
    }
}
