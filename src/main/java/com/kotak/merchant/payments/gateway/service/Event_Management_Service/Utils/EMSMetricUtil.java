package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EMSMetricUtil {
    private final MeterRegistry registry;

    public EMSMetricUtil(MeterRegistry registry) {
        this.registry = registry;
    }

    public TimeIt timeIt(String message) {
        return new TimeIt(message, registry);
    }

    public Counter counter(String name, String... tags) {
        return registry.counter(name, tags);
    }

    //    Gauge metric represent a single numerical value that can arbitrarily go up and down
    //    We have transaction lag value which can go in negative also.
    public Gauge gauge(String name, long value) {
        return Gauge.builder(name, value, Long::valueOf).register(registry);
    }
}
