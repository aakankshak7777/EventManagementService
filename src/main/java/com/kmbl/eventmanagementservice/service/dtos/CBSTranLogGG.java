package com.kmbl.eventmanagementservice.service.dtos;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.model.CBSTranLogData;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record CBSTranLogGG(
        @NotNull String transactionId,
        @NotNull String type,
        CBSTranLogData metaData,
        EventStatus eventStatus,
        EventName createdBy) {}
