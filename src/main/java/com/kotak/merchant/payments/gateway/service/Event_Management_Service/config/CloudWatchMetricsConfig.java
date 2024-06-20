package com.kotak.merchant.payments.gateway.service.Event_Management_Service.config;


import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.opentelemetry.instrumentation.awssdk.v2_2.AwsSdkTelemetry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryProperties;
import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryPropertiesConfigAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.metrics.MetricPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.net.URI;
import java.util.List;

/**
 * CloudWatch integration is present only in production at the moment.
 * <p>
 * TODO: Turn it on UAT as well.
 */
@Profile("prod")
@Slf4j
@Configuration
public class CloudWatchMetricsConfig {

    @Value("${aws.cloudwatch.endpointOverride}")
    private String cwEndpointOverride;

    @Value("${app.cw.namespace}")
    private String namespace;

    @SneakyThrows
    @Bean
    @DependsOn("dependencyPlaceholder")
    public CloudWatchAsyncClient cloudWatchAsyncClient(
            AwsSdkTelemetry awsOtel, AwsCredentialsProvider credentialsProvider, Region awsRegion) {
        var builder = CloudWatchAsyncClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(awsRegion)
                .overrideConfiguration(c -> c.addExecutionInterceptor(awsOtel.newExecutionInterceptor()));
        if (cwEndpointOverride != null && !"ignore".equals(cwEndpointOverride)) {
            log.info("CloudWatch endpoint override: {}", cwEndpointOverride);
            builder.endpointOverride(new URI(cwEndpointOverride));
        }
        return builder.build();
    }

    @Bean
    public LogbackMetrics logbackMetrics(MeterRegistry meterRegistry) {
        var lb = new LogbackMetrics();
        lb.bindTo(meterRegistry);
        return lb;
    }

    @Bean
    public CompositeMeterRegistry meterRegistry(CloudWatchMeterRegistry cwMeterRegistry) {
        return new CompositeMeterRegistry(Clock.SYSTEM, List.of(cwMeterRegistry));
    }

    @Bean
    public CloudWatchMeterRegistry cwMeterRegistry(CloudWatchAsyncClient cloudWatchAsyncClient) {
        var config = new MicrometerCloudWatchConfig(namespace);
        return new CloudWatchMeterRegistry(config, Clock.SYSTEM, cloudWatchAsyncClient);
    }

    @Bean
    public MetricPublisher sdkClientMetricsPublisher(CloudWatchMeterRegistry cloudWatchMeterRegistry) {
        return new AwsSdkClientMetricsPublisherConfig(cloudWatchMeterRegistry);
    }

    public static class MicrometerCloudWatchConfig extends StepRegistryPropertiesConfigAdapter<StepRegistryProperties>
            implements CloudWatchConfig {

        private final String namespace;

        public MicrometerCloudWatchConfig(String namespace) {
            super(new StepRegistryProperties() {
            });
            this.namespace = namespace;
        }

        @Override
        public String namespace() {
            return namespace;
        }

        @Override
        public int batchSize() {
            return 1000;
        }
    }
}
