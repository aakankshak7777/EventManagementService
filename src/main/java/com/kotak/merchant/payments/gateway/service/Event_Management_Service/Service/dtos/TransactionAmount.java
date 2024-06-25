package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.dtos;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record TransactionAmount(@NonNull BigDecimal value, @NonNull Currency currency) {

    public boolean isEquivalentTo(TransactionAmount other) {
        return value.compareTo(other.value()) == 0 && Objects.equals(currency, other.currency());
    }
}
