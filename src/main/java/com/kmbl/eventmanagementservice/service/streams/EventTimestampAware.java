package com.kmbl.eventmanagementservice.service.streams;

import java.time.Instant;

public interface EventTimestampAware {

    Instant eventTimestamp();
}
