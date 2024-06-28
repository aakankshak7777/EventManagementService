package com.kmbl.eventmanagementservice.service.streams.producers;


import com.kmbl.eventmanagementservice.utils.MessagePartitioner;
import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

@RequiredArgsConstructor
@Slf4j
public class KafkaProducerFactory<T> {

    private final String topic;

    private final String bootstrapServers;

    private final File sslTrustStoreLocation;

    private final String securityProtocol;

    private final Class<? extends Serializer> serializerClass;

    public KafkaProducer<String, T> newProducer(int producerId) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializerClass.getName());
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MessagePartitioner.class.getName());
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "5000");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, getClientId(producerId));
        if (sslTrustStoreLocation != null) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
            props.put(
                    SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
                    sslTrustStoreLocation.toPath().toString());
        }
        if ("AWS_MSK_IAM".equals(securityProtocol)) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            props.put(SaslConfigs.SASL_MECHANISM, "AWS_MSK_IAM");
            props.put(SaslConfigs.SASL_JAAS_CONFIG, "software.amazon.msk.auth.iam.IAMLoginModule required;");
            props.put(
                    SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS,
                    "software.amazon.msk.auth.iam.IAMClientCallbackHandler");
        }

        // Needed to avoid out of order messages during retries. If we have this value more than 1,
        // then the earlier batch may be retried after a later batch in case of a failure for the
        // earlier one. Keeping this to one ensures that no out of order publishing happens to
        // a partition
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");

        // Ack from one broker is good enough as we don't need guaranteed persistence
        // from Kafka. Anything less than that does not throw up errors on unavailability, etc.
        props.put(ProducerConfig.ACKS_CONFIG, "1");

        // Don't wait for more than 50ms to batch up messages before publishing
        // This directly impacts how long a staged transaction takes to be finally synced to CBS
        props.put(ProducerConfig.LINGER_MS_CONFIG, "50");

        // No need of idempotence while publishing to queue. Our consumers can handle duplicate records
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "false");

        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");

        log.info("Kafka Publisher properties: {}", props);
        var producer = new KafkaProducer<String, T>(props);
        var kmetrics = new KafkaClientMetrics(producer);
        return producer;
    }

    private String getClientId(int partitionId) {
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostName = "unknown";
        }

        return String.format(
                "rts-producer-%s-%s-%d-%d",
                topic, hostName, ProcessHandle.current().pid(), partitionId);
    }

    public String topic() {
        return topic;
    }
}
