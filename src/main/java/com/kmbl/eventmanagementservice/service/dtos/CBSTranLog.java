package com.kmbl.eventmanagementservice.service.dtos;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record CBSTranLog(
        @NotNull String transactionId,
        @NotNull String type,
        CBSTranLogData metaData,
        EventStatus eventStatus,
        EventName createdBy) {}
