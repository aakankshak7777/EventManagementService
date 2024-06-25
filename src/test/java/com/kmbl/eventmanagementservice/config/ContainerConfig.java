package com.kmbl.eventmanagementservice.config;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.CLOUDWATCH;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import com.redis.testcontainers.RedisContainer;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.Q2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.DockerHealthcheckWaitStrategy;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@Configuration
public class ContainerConfig {

    // https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing.testcontainers
    // https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testcontainers

    @Value("${dependencies.cbs.iso-spec.file}")
    private String specFile;

    @Value("${dependencies.cbs.iso-config.test-deploy-dir}")
    private String deployDir;

    @Value("${dependencies.cbs.port}")
    private String port;

    @Bean
    public KafkaContainer kafkaContainer() {
        var imageName = DockerImageName.parse("confluentinc/cp-kafka:7.5.2");
        var kafka = new KafkaContainer(imageName)
                .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "false")
                .withEnv("TOPIC_AUTO_CREATE", "false");
        configureForReuse(kafka);
        return kafkaContainer(true);
    }

    public KafkaContainer kafkaContainer(boolean createTopic) {

        var imageName = DockerImageName.parse("confluentinc/cp-kafka:7.5.2");
        var kafka = new KafkaContainer(imageName)
                .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "false")
                .withEnv("TOPIC_AUTO_CREATE", "false");
        configureForReuse(kafka);

        // We create the topics right after the container spins to avoid weird stuff
        // happening with consumer and topics.
        kafka.waitingFor(new DockerHealthcheckWaitStrategy());
        System.out.println("Kafka container up. Creating topics if needed.");
        System.setProperty("rts-transactions.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-gam-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-aas-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-smt-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-cam-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-tam-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());

        System.setProperty("rts-cbs-gsp-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-csp-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-ext-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-lrct-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-setvar-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-ssp-events.kafka.consumer.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("rts-cbs-all-events.bootstrap-servers", kafka.getBootstrapServers());
        System.out.println("[CONFIG] Kafka Bootstrap Servers: " + kafka.getBootstrapServers());

        return kafka;
    }

    //    private void createAndSetTopic(String bootstrapServers, String propertyName, String topicPrefix) {
    //        var topicName = randStr(topicPrefix, 16);
    //        KafkaAdminUtils.createTopic(bootstrapServers, topicName);
    //        System.setProperty(propertyName, topicName);
    //    }

    @Bean
    public RedisContainer redisContainer() {
        var redis = new RedisContainer(DockerImageName.parse("redis:7-alpine"));
        configureForReuse(redis);
        System.setProperty("throttler.redis.nodes", redis.getRedisURI());
        System.out.println("[CONFIG] Redis URI: " + redis.getRedisURI());
        return redis;
    }

    @Bean
    public LocalStackContainer localStack() {

        var ls = new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
                .withServices(DYNAMODB, CLOUDWATCH, S3);
        configureForReuse(ls);
        System.setProperty("aws.accessKeyOverride", ls.getAccessKey());
        System.setProperty("aws.secretKeyOverride", ls.getSecretKey());
        System.setProperty(
                "aws.dynamodb.endpointOverride",
                ls.getEndpointOverride(DYNAMODB).toString());
        System.setProperty(
                "aws.cloudwatch.endpointOverride",
                ls.getEndpointOverride(CLOUDWATCH).toString());
        System.setProperty("aws.s3.endpointOverride", ls.getEndpointOverride(S3).toString());
        System.out.println("[CONFIG] AWS Access Key: " + ls.getAccessKey());
        System.out.println("[CONFIG] AWS Secret Key: " + ls.getSecretKey());
        System.out.println("[CONFIG] DynamoDB endpoint: " + ls.getEndpointOverride(DYNAMODB));
        System.out.println("[CONFIG] CloudWatch endpoint: " + ls.getEndpointOverride(CLOUDWATCH));
        System.out.println("[CONFIG] S3 endpoint: " + ls.getEndpointOverride(S3));
        return ls;
    }

    @Primary
    @Bean
    public int dependencyPlaceholder(
            LocalStackContainer localStack, KafkaContainer kafkaContainer, RedisContainer redisContainer) {
        return -1;
    }

    @Bean
    public Q2 testq2() {
        System.setProperty("dependencies.cbs.iso-spec.file", specFile);
        System.setProperty("dependencies.cbs.port", port);
        // System.setProperty("dependencies.cbs.iso-config.test-deploy-dir", deployDir);
        var q2 = new Q2(deployDir);
        Thread thread = new Thread(q2);
        thread.start();
        while (!q2.ready()) {
            ISOUtil.sleep(10);
        }
        return q2;
    }

    private static <T extends GenericContainer<T>> void configureForReuse(GenericContainer<T> container) {
        container.withReuse(true);
        if (!container.isRunning()) {
            container.start();
        }
    }
}
