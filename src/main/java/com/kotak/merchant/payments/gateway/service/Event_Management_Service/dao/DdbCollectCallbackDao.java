package com.kotak.merchant.payments.gateway.service.Event_Management_Service.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EMSMetricUtil;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.dao.models.DbCollectCallback;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.exceptions.CollectCallbackExistsException;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.model.CollectCallback;
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
    private final EMSMetricUtil metricUtil;
    private final ObjectMapper mapper = new ObjectMapper();

    public DdbCollectCallbackDao(DynamoDbEnhancedClient ddb, EMSMetricUtil metricUtil) {
        this.ddb = ddb;
        this.table = ddb.table(DbCollectCallback.TABLE_NAME, TableSchema.fromBean(DbCollectCallback.class));
        this.metricUtil = metricUtil;
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
        try (var ignored = metricUtil.timeIt("TransactionDao.etByPartitionAndSortKey.Latency")) {
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
