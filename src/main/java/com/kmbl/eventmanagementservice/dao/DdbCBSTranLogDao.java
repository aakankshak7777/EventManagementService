package com.kmbl.eventmanagementservice.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmbl.eventmanagementservice.dao.models.DbCBSTranLog;
import com.kmbl.eventmanagementservice.exceptions.CBSTranLogExistsException;
import com.kmbl.eventmanagementservice.exceptions.CollectCallbackExistsException;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLog;
import com.kmbl.eventmanagementservice.utils.EpochProvider;

import java.util.Optional;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

public class DdbCBSTranLogDao implements CBSTranLogDao {
    public static final int MAX_ITEM_SIZE = 100;
    public static final int DEFAULT_ITEM_SIZE = 10;
    private final DynamoDbEnhancedClient ddb;
    private final DynamoDbTable<DbCBSTranLog> table;
    private final ObjectMapper mapper = new ObjectMapper();

    public DdbCBSTranLogDao(DynamoDbEnhancedClient ddb) {
        this.ddb = ddb;
        this.table = ddb.table(DbCBSTranLog.TABLE_NAME, TableSchema.fromBean(DbCBSTranLog.class));
    }

    @Override
    public void create(CBSTranLog cbsTranLog) {
        try {
            var dbCBSTranLog = DbCBSTranLog.from(cbsTranLog);
            table.putItem(b -> b.conditionExpression(QueryUtils.primaryKeyNotExists(table.tableSchema()))
                    .item(dbCBSTranLog));
        } catch (ConditionalCheckFailedException e) {
            throw new CBSTranLogExistsException(cbsTranLog.transactionId(), e);
        }
    }

    @Override
    public Optional<CBSTranLog> getByTransactionIdAndType(String transactionId, String type) {
        try {
            var res = table.getItem(r -> r.consistentRead(true)
                    .key(k -> k.partitionValue(transactionId).sortValue(type)));
            return Optional.ofNullable(res).map(DbCBSTranLog::to);
        } catch (CBSTranLogExistsException e) {
            throw new CBSTranLogExistsException(e);
        }
    }

    @Override
    public void update(CBSTranLog cbsTranLog) {
        try {
            var dbCBSTranLog = DbCBSTranLog.from(cbsTranLog);
            table.updateItem(dbCBSTranLog);
        } catch (ConditionalCheckFailedException e) {
            throw new CollectCallbackExistsException(cbsTranLog.transactionId(), e);
        }
    }
}
