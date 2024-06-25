package com.kmbl.eventmanagementservice.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmbl.eventmanagementservice.dao.models.DbCollectCallback;
import com.kmbl.eventmanagementservice.exceptions.CollectCallbackExistsException;
import com.kmbl.eventmanagementservice.model.CollectCallback;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

public class DdbCollectCallbackDao implements CollectCallbackDao {

    public static final int MAX_ITEM_SIZE = 100;
    public static final int DEFAULT_ITEM_SIZE = 10;
    private final DynamoDbEnhancedClient ddb;
    private final DynamoDbTable<DbCollectCallback> table;
    private final ObjectMapper mapper = new ObjectMapper();

    public DdbCollectCallbackDao(DynamoDbEnhancedClient ddb) {
        this.ddb = ddb;
        this.table = ddb.table(DbCollectCallback.TABLE_NAME, TableSchema.fromBean(DbCollectCallback.class));
    }

    @Override
    public void create(CollectCallback collectCallback) {
        try {
            var dbCollectCallback = DbCollectCallback.from(collectCallback);
            table.putItem(b -> b.conditionExpression(QueryUtils.primaryKeyNotExists(table.tableSchema()))
                    .item(dbCollectCallback));
        } catch (ConditionalCheckFailedException e) {
            throw new CollectCallbackExistsException(collectCallback.transactionId(), e);
        }
    }

    @Override
    public List<CollectCallback> getByTransactionId(String transactionId) {
        var res =
                table.query(r -> r.queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(transactionId))));
        return convertIntoList(res);
    }

    @Override
    public Optional<CollectCallback> getByTransactionIdAndType(String transactionId, String type) {
        try  {
            var res = table.getItem(r -> r.consistentRead(true)
                    .key(k -> k.partitionValue(transactionId).sortValue(type)));
            return Optional.ofNullable(res).map(DbCollectCallback::to);
        } catch (ConditionalCheckFailedException e) {
            throw new CollectCallbackExistsException(e);
        }
    }

    private List<CollectCallback> convertIntoList(PageIterable<DbCollectCallback> dbTransactions) {
        return dbTransactions.stream()
                .flatMap(page -> page.items().stream())
                .map(DbCollectCallback::to)
                .collect(Collectors.toList());
    }
}
