package com.kotak.merchant.payments.gateway.service.testutils;

import java.time.Instant;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class RandUtils {

    private static Random random = new Random();

    public static String randStr(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String randStr(String prefix, int length) {
        return prefix + RandomStringUtils.randomAlphanumeric(length);
    }

    public static String randStr(int length, String sufix) {
        return RandomStringUtils.randomAlphanumeric(length) + sufix;
    }

    public static Long randLong() {
        return random.nextLong();
    }

    public static long randEpoch() {
        return RandomUtils.nextLong(100000L, 1738814487000L);
    }

    public static Instant randInstant() {
        return Instant.ofEpochMilli(RandomUtils.nextLong(
                100000L, Instant.parse("3000-12-12T10:15:30.00Z").toEpochMilli()));
    }

    public static long currentEpoch() {
        return Instant.now().toEpochMilli();
    }

    public static long randOffset() {
        return RandomUtils.nextLong(0, 1_000_000_000L);
    }

    public static int randPartition() {
        return RandomUtils.nextInt(0, 1_000);
    }

    public static String randKafkaKey() {
        return randStr(10);
    }

    public static String randKafkaTopic() {
        return randStr(32);
    }

    public static Instant randInstantSecond() {
        var instant = randInstant();
        return Instant.ofEpochSecond(instant.getEpochSecond());
    }
}
