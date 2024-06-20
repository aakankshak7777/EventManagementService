package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * This class times the code from the time of creation of its instance till close() is invoked. It
 * is a stop-gap for dumping latencies in logs to help in debugging perf problems till we are fully
 * integrated with AppDynamics.
 */
@Slf4j
public class TimeIt implements Closeable {

    private final String message;
    private final long start;
    private final MeterRegistry registry;

    public TimeIt(String message, MeterRegistry registry) {
        this.message = message;
        this.start = System.nanoTime();
        this.registry = registry;
    }

    public void emitTimeMetric(String message, long totalTime) {
        if (registry != null) {
            Timer timer = getTimer(message);
            timer.record(totalTime, TimeUnit.MILLISECONDS);
        }
    }

    public Timer getTimer(String message) {
        return Timer.builder(message)
                .publishPercentiles(1.0, 0.99, 0.95, 0.9, 0.5)
                .register(registry);
    }

    @Override
    public void close() {
        var end = System.nanoTime();
        var totalTime = (end - start) / 1000_000L;
        if (registry != null) {
            Timer timer = getTimer(message);
            timer.record(totalTime, TimeUnit.MILLISECONDS);
        }
    }
}
