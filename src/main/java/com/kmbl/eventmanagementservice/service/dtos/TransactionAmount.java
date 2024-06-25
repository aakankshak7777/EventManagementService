package com.kmbl.eventmanagementservice.service.dtos;

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
