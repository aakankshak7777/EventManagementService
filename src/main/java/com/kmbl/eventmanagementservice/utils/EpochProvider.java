package com.kmbl.eventmanagementservice.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Component
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
