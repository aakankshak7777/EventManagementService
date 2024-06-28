package com.kmbl.eventmanagementservice.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmbl.eventmanagementservice.dao.models.DbCBSTranLogGG;
import com.kmbl.eventmanagementservice.exceptions.CollectCallbackExistsException;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogGG;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

public class DdbCBSTranLogGGDao implements CBSTranLogGGDao {
public static final int MAX_ITEM_SIZE = 100;
public static final int DEFAULT_ITEM_SIZE = 10;
private final DynamoDbEnhancedClient ddb;
private final DynamoDbTable<DbCBSTranLogGG> table;
private final ObjectMapper mapper = new ObjectMapper();

public DdbCBSTranLogGGDao(DynamoDbEnhancedClient ddb) {
    this.ddb = ddb;
    this.table = ddb.table(DbCBSTranLogGG.TABLE_NAME, TableSchema.fromBean(DbCBSTranLogGG.class));
}

@Override
public void create(CBSTranLogGG cbsTranLogGG) {
    EpochProvider epochProvider = new EpochProvider();
    try {
        var dbCBSTranLogGG = DbCBSTranLogGG.from(cbsTranLogGG, epochProvider.now(), epochProvider.now());
        table.putItem(b -> b.conditionExpression(QueryUtils.primaryKeyNotExists(table.tableSchema()))
                .item(dbCBSTranLogGG ));
    } catch (ConditionalCheckFailedException e) {
        throw new CollectCallbackExistsException(cbsTranLogGG.transactionId(), e);
    }
}

@Override
public List<CBSTranLogGG> getByTransactionId(String transactionId) {
    var res =
            table.query(r -> r.queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(transactionId))));
    return convertIntoList(res);
}

@Override
public Optional<CBSTranLogGG> getByTransactionIdAndType(String transactionId, String type) {
    try  {
        var res = table.getItem(r -> r.consistentRead(true)
                .key(k -> k.partitionValue(transactionId).sortValue(type)));
        return Optional.ofNullable(res).map(DbCBSTranLogGG::to);
    } catch (ConditionalCheckFailedException e) {
        throw new CollectCallbackExistsException(e);
    }
}

private List<CBSTranLogGG> convertIntoList(PageIterable<DbCBSTranLogGG> dbTransactions) {
    return dbTransactions.stream()
            .flatMap(page -> page.items().stream())
            .map(DbCBSTranLogGG::to)
            .collect(Collectors.toList());
    }
}
