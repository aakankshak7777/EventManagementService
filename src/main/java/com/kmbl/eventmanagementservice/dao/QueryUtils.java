package com.kmbl.eventmanagementservice.dao;

import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class QueryUtils {

    public static <T> Expression primaryKeyNotExists(TableSchema<T> schema) {
        return Expression.builder()
                .expression("attribute_not_exists(#pk)")
                .putExpressionName("#pk", schema.tableMetadata().primaryPartitionKey())
                .build();
    }
}
