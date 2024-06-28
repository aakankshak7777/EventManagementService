package com.kmbl.eventmanagementservice.dao.models;

import com.kmbl.eventmanagementservice.dao.dynamodb.converter.InstantConverter;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogGG;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbCBSTranLogGG {

    public static final String TABLE_NAME = "CBSTranLogGG";
    public static final String ATTR_TRANSACTION_ID = "transactionId";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_VERSION = "version";
    public static final String ATTR_CREATED_TIME = "createdTime";
    public static final String ATTR_UPDATED_TIME =  "updatedTime";
    private String transactionId;
    private String type;
    private Instant createdTime;
    private Instant updatedTime;
    private EventStatus eventStatus;
    private EventName createdBy;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(ATTR_TRANSACTION_ID)
    public String getTransactionId() {
        return transactionId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute(ATTR_TYPE)
    public String getType() {
        return type;
    }

    @DynamoDbConvertedBy(InstantConverter.class)
    @DynamoDbAttribute(ATTR_CREATED_TIME)
    public Instant getCreatedTime() {
        return this.createdTime;
    }

    @DynamoDbConvertedBy(InstantConverter.class)
    @DynamoDbAttribute(ATTR_UPDATED_TIME)
    private Instant getUpdatedTime() {
        return this.updatedTime;
    }

    public CBSTranLogGG to() {
        return CBSTranLogGG.builder()
                .transactionId(transactionId)
                .type(type)
                .createdBy(createdBy)
                .eventStatus(eventStatus)
                .build();
    }

    public static DbCBSTranLogGG from(CBSTranLogGG collectCallback, Instant createdAt, Instant updatedAt) {
        DbCBSTranLogGG callback = new DbCBSTranLogGG();
        callback.setTransactionId(collectCallback.transactionId());
        callback.setType(collectCallback.type());
        callback.setCreatedTime(createdAt);
        callback.setUpdatedTime(updatedAt);
        callback.setEventStatus(collectCallback.eventStatus());
        callback.setCreatedBy(collectCallback.createdBy());
        return callback;
    }
}
