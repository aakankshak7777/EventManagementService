package com.kotak.merchant.payments.gateway.service.Event_Management_Service.repository.dao.models;

import java.math.BigDecimal;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.dtos.Currency;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.dtos.TransactionAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbTransactionAmount {

    private BigDecimal value;

    private Currency currency;

    public TransactionAmount to() {
        return TransactionAmount.builder().value(value).currency(currency).build();
    }

    public static DbTransactionAmount from(TransactionAmount transactionAmount) {
        if (transactionAmount == null) {
            return null;
        }
        return DbTransactionAmount.builder()
                .value(transactionAmount.value())
                .currency(transactionAmount.currency())
                .build();
    }
}
