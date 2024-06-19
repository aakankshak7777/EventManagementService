// package com.kotak.merchant.payments.gateway.service.Config;
//
// package com.kmbl.realtimetransactionservice.integration.config;
//
// import com.kmbl.realtimetransactionservice.repository.dynamodb.DynamoDbUtils;
// import jakarta.annotation.PostConstruct;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.test.context.TestConfiguration;
// import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
//
// @Slf4j
// @TestConfiguration
// public class SetupConfig {
//
//    @Value("${awsS3.bucket.name}")
//    private String bucketName;
//
//    private final DynamoDbClient ddbClient;
//
//    public SetupConfig(DynamoDbClient ddbClient) {
//        this.ddbClient = ddbClient;
//    }
//
//    @PostConstruct
//    public void setupTables() {
//
//        DynamoDbUtils.createAllTables(ddbClient);
//        log.info("Finished creating DynamoDB tables");
//    }
// }
//
