package com.kmbl.eventmanagementservice.Config;

import com.kmbl.eventmanagementservice.testUtils.DynamoDbUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Slf4j
@TestConfiguration
public class SetupConfig {

    private final DynamoDbClient ddbClient;

    public SetupConfig(DynamoDbClient ddbClient) {
        this.ddbClient = ddbClient;
    }

    @PostConstruct
    public void setupTables() {
        DynamoDbUtils.createAllTables(ddbClient);
        log.info("Finished creating DynamoDB tables");
    }
}
