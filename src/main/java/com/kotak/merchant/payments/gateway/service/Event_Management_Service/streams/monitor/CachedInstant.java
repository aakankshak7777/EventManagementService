package com.kotak.merchant.payments.gateway.service.Event_Management_Service.streams.monitor;

import java.time.Instant;

/**
 * Represents a cached timestamp
 *
 * @param value The timestamp value
 * @param asOf  The last thing the timestamp value was refreshed
 */
public record CachedInstant(Instant value, Instant asOf) {}
