package com.kmbl.eventmanagementservice.dao.models;

import com.kmbl.eventmanagementservice.dao.dynamodb.converter.InstantConverter;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLog;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.UpdateBehavior;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbCBSTranLog {

    public static final String TABLE_NAME = "CBSTranLog";
    public static final String ATTR_TRANSACTION_ID = "transactionId";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_VERSION = "version";
    public static final String ATTR_CREATEDAT = "createdAt";
    public static final String ATTR_UPDATEDAT=  "updatedAt";
    private String transactionId;
    private String type;
    private DbCBSTranLogData dbCBSTranLog;
    private Instant createdAt;
    private Instant updatedAt;
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

    @DynamoDbAttribute(ATTR_CREATEDAT)
    @DynamoDbConvertedBy(InstantConverter.class)
    @DynamoDbUpdateBehavior(UpdateBehavior.WRITE_IF_NOT_EXISTS)
    public Instant getCreatedAt() {
        return this.createdAt == null ? Instant.now() : this.createdAt;
    }

    @DynamoDbAttribute(ATTR_UPDATEDAT)
    @DynamoDbConvertedBy(InstantConverter.class)
    @DynamoDbUpdateBehavior(UpdateBehavior.WRITE_ALWAYS)
    public Instant getUpdatedAt() {
        return Instant.now();
    }
    public CBSTranLog to() {
        return CBSTranLog.builder()
                .transactionId(transactionId)
                .type(type)
                .metaData(dbCBSTranLog!= null ? dbCBSTranLog.toCBSTranLogData() : null)
                .createdBy(createdBy)
                .eventStatus(eventStatus)
                .build();
    }

    public static DbCBSTranLog from(CBSTranLog cbsTranLog) {
        DbCBSTranLog callback = new DbCBSTranLog();
        callback.setTransactionId(cbsTranLog.transactionId());
        callback.setType(cbsTranLog.type());
        callback.setEventStatus(cbsTranLog.eventStatus());
        callback.setCreatedBy(cbsTranLog.createdBy());
        callback.setDbCBSTranLog(DbCBSTranLogData.from(cbsTranLog.metaData()));
        return callback;
    }
}
