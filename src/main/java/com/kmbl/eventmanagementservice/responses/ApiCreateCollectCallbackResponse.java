package com.kmbl.eventmanagementservice.responses;

import com.kmbl.eventmanagementservice.enums.CollectCallbackStatus;
import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record ApiCreateCollectCallbackResponse(
        @NonNull CollectCallbackStatus collectCallbackStatus, String message) {}
