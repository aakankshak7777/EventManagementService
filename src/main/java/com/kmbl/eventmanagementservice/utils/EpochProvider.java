package com.kmbl.eventmanagementservice.utils;

import java.time.Instant;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class EpochProvider {
    public long currentEpoch() {
        return System.currentTimeMillis();
    }

    public Instant now() {
        return Instant.now();
    }

    public long currentEpochOnOrAfter(long epoch) {
        return Math.max(currentEpoch(), epoch);
    }
}
