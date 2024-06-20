package com.kotak.merchant.payments.gateway.service.Config;

import com.kotak.merchant.payments.gateway.service.utils.DynamoDbUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Slf4j
@Configuration
public class SetupConfig {

    @Value("${awsS3.bucket.name}")
    private String bucketName;

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
