package com.kotak.merchant.payments.gateway.service.Event_Management_Service.config;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EMSMetricUtil;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EpochProvider;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.dao.DdbCollectCallbackDao;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NoArgsConstructor;
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

import java.net.URI;

@Configuration
@Slf4j
@NoArgsConstructor
public class DynamoDbConfig {

    @Value("${aws.dynamodb.endpointOverride}")
    private String dynamodbEndpoint;

    @Value("${aws.region}")
    private String region;

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
    public EMSMetricUtil emsMetricUtil(MeterRegistry registry) {
        return new EMSMetricUtil(registry);
    }

    @Bean
    public EpochProvider epochProvider() {
        return new EpochProvider();
    }

    @Bean
    public DdbCollectCallbackDao collectCallbackDao(DynamoDbEnhancedClient ddb, EMSMetricUtil metricUtil) {
        return new DdbCollectCallbackDao(ddb, metricUtil);
    }
}
