package com.kotak.merchant.payments.gateway.service.Config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import com.redis.testcontainers.RedisContainer;

@Slf4j
@Configuration
public class ContainerConfig {


    @Bean
    @ServiceConnection
    public KafkaContainer kafkaContainer() {
        return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))
                .withKraft()
                .withReuse(true);
    }

    @Bean
    public LocalStackContainer localStack() {

        var ls = new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
                .withServices(DYNAMODB);
        configureForReuse(ls);
        System.setProperty(
                "aws.dynamodb.endpointOverride",
                ls.getEndpointOverride(DYNAMODB).toString());
        log.debug("[CONFIG] AWS Access Key: " + ls.getAccessKey());
        log.debug("[CONFIG] AWS Secret Key: " + ls.getSecretKey());
        log.debug("[CONFIG] DynamoDB endpoint: " + ls.getEndpointOverride(DYNAMODB));
        return ls;
    }

    @Bean
    public RedisContainer redisContainer() {
        var redis = new RedisContainer(DockerImageName.parse("redis:7-alpine")).withExposedPorts(6379);
        configureForReuse(redis);
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
        System.out.println("[CONFIG] Redis Host: " + redis.getHost());
        return redis;
    }

    @Primary
    @Bean
    public int dependencyPlaceholder(LocalStackContainer localStack) {
        return -1;
    }

    private static <T extends GenericContainer<T>> void configureForReuse(GenericContainer<T> container) {
        container.withReuse(true);
        if (!container.isRunning()) {
            container.start();
        }
    }
}

