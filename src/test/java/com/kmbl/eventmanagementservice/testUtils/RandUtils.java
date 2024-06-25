package com.kmbl.eventmanagementservice.testUtils;

import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class RandUtils {

    private static final Random random = new Random();

    public static String randStr(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String randStr(String prefix, int length) {
        return prefix + RandomStringUtils.randomAlphanumeric(length);
    }

    public static String randStr(int length, String sufix) {
        return RandomStringUtils.randomAlphanumeric(length) + sufix;
    }

    public static long randEpoch() {
        return RandomUtils.nextLong(100000L, 1738814487000L);
    }


}
