package com.kmbl.eventmanagementservice.dao.models;

import com.kmbl.eventmanagementservice.service.dtos.Currency;
import com.kmbl.eventmanagementservice.service.dtos.TransactionAmount;
import java.math.BigDecimal;
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
