package com.kotak.merchant.payments.gateway.service.Event_Management_Service.responses;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.CollectCallbackStatus;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.model.CollectCallback;
import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record ApiCreateCollectCallbackResponse(
        @NonNull CollectCallbackStatus collectCallbackStatus, CollectCallback collectCallback) {}
