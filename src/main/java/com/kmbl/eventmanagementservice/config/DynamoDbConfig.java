package com.kmbl.eventmanagementservice.config;

import com.kmbl.eventmanagementservice.dao.DdbCollectCallbackDao;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import java.net.URI;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.extensions.VersionedRecordExtension;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@Slf4j
@NoArgsConstructor
public class DynamoDbConfig {

    @Value("${aws.dynamodb.endpointOverride}")
    private String dynamodbEndpoint;

    @Value("${aws.region}")
    private String region;

    @SneakyThrows
    @Bean
    @DependsOn("dependencyPlaceholder")
    public DynamoDbClient getDynamoDbClient() {
        var builder = DynamoDbClient.builder().credentialsProvider(DefaultCredentialsProvider.create());

        if (dynamodbEndpoint != null && !dynamodbEndpoint.isBlank()) {
            builder.region(Region.of(region)).endpointOverride(URI.create(dynamodbEndpoint));
            log.info("DynamoDB Client initialized in region " + region);
            log.warn("DynamoDB Client ENDPOINT overridden to " + dynamodbEndpoint);
        }
        return builder.build();
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient(DynamoDbClient ddbc) {
        return DynamoDbEnhancedClient.builder()
                .extensions(VersionedRecordExtension.builder().build())
                .dynamoDbClient(ddbc)
                .build();
    }


    @Bean
    public EpochProvider epochProvider() {
        return new EpochProvider();
    }

    @Bean
    public DdbCollectCallbackDao collectCallbackDao(DynamoDbEnhancedClient ddb) {
        return new DdbCollectCallbackDao(ddb);
    }
}
