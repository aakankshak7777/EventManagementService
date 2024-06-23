package com.kotak.merchant.payments.gateway.service.utils;

import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Slf4j
public class DynamoDbUtils {

    public static DynamoDbEnhancedClient newDynamoDbLocal() {
        var ddbEmbedded = DynamoDBEmbedded.create();
        var ddbClient = ddbEmbedded.dynamoDbClient();
        createAllTables(ddbClient);
        return DynamoDbEnhancedClient.builder().dynamoDbClient(ddbClient).build();
    }

    @SneakyThrows
    public static void createAllTables(DynamoDbClient ddbClient) {
        System.setProperty("sqlite4java.library.path", "native-libs");
        var ddb = DynamoDbEnhancedClient.builder().dynamoDbClient(ddbClient).build();
        var ddbModelClasses = ClassPath.from(ClassLoader.getSystemClassLoader())
                .getTopLevelClassesRecursive(
                        "com.kotak.merchant.payments.gateway.service.Event_Management_Service.dao")
                .stream()
                .map(ClassInfo::load)
                .filter(c -> c.getDeclaredAnnotation(DynamoDbBean.class) != null)
                .toList();

        try (var waiter = DynamoDbWaiter.builder().client(ddbClient).build()) {
            ddbModelClasses.forEach(mc -> {
                try {
                    log.info("Processing dynamodb model: {}", mc);
                    var tableName = (String) mc.getDeclaredField("TABLE_NAME").get(null);
                    var schema = TableSchema.fromBean(mc);
                    var table = ddb.table(tableName, schema);
                    log.info("Creating DynamoDB table: {} for model: {}", tableName, mc);
                    table.createTable();
                    waiter.waitUntilTableExists(r -> r.tableName(tableName));
                } catch (NoSuchFieldException e) {
                    log.info("No table name found for: {}. Skipping creation", mc);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (ResourceInUseException e) {
                    log.info("Table already exists for model: {}", mc);
                }
            });
        }
    }
}
