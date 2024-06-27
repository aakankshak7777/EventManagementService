package com.kmbl.eventmanagementservice.unittest.service.streams.consumers;

import com.kmbl.eventmanagementservice.Config.ContainerConfig;
import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import com.kmbl.eventmanagementservice.service.streams.consumers.CBSTranLogConsumer;
import com.kmbl.eventmanagementservice.service.streams.consumers.DlqPublisher;
import com.kmbl.eventmanagementservice.service.streams.consumers.MessageConsumer;
import com.kmbl.eventmanagementservice.service.streams.consumerservice.ConsumerService;
import com.kmbl.eventmanagementservice.service.streams.serializers.CBSTransactionLogsDeserializer;
import com.kmbl.eventmanagementservice.testUtils.KafkaAdminUtils;
import com.kmbl.eventmanagementservice.testUtils.KafkaTestUtils;
import com.kmbl.eventmanagementservice.testUtils.UnitDataGenUtils;
import com.kmbl.eventmanagementservice.unittest.DynamoDbSetupBase;
import com.kmbl.eventmanagementservice.utils.EpochProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.kafka.receiver.ReceiverRecord;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randEpoch;
import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randStr;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.lenient;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class DlqPublisherTest extends DynamoDbSetupBase {

    private static final ContainerConfig containerConfig = new ContainerConfig();
    private static String bootstrapServers;
    private String topic;
    private String dlqTopic;
    protected String groupId;
    private ConsumerService svc;
    private long currEpoch;
    private final Duration MAX_WAIT_DURATION = Duration.ofSeconds(100);

    @Mock
    private EpochProvider epochProvider;

    @BeforeAll
    public static void setUpBeforeClass() {
        var container = containerConfig.kafkaContainer(false);
        bootstrapServers = container.getBootstrapServers();
    }

    @BeforeEach
    public void setUp() {
        svc = new ConsumerService();
        this.topic = randStr("test-", 32);
        this.dlqTopic = randStr("test-", 32);
        this.groupId = randStr("test-", 32);
        System.out.printf("Creating topic: %s\n", topic);
        KafkaAdminUtils.createTopic(bootstrapServers, this.topic);
        System.out.printf("Creating dlqTopic: %s\n", dlqTopic);
        KafkaAdminUtils.createTopic(bootstrapServers, this.dlqTopic);
        currEpoch = randEpoch();
        lenient().when(epochProvider.currentEpoch()).thenReturn(currEpoch);
    }

    @AfterEach
    public void cleanUpBase() {
        System.out.printf("Deleting topic: %s\n", this.topic);
        KafkaAdminUtils.deleteTopics(bootstrapServers, this.topic);
        System.out.printf("Deleting dlqTopic: %s\n", this.dlqTopic);
        KafkaAdminUtils.deleteTopics(bootstrapServers, this.dlqTopic);
    }

    @Test
    public void cbsTranLog_failedEventPushedInDlq() {
        var cbsTranLogConsumer = new CBSTranLogConsumer(svc);
        var cbsTranLogEvent = UnitDataGenUtils.getInsertDataEvent();
        cbsTranLogEvent.setAfter(null);
        cbsTranLogEvent.setOpType("U");

        try (var kafkaProducer = new KafkaProducer<String, CBSTransactionLogs>(getDlqPublisherProperties())) {
            var dlqPublisher = new DlqPublisher<CBSTransactionLogs>(kafkaProducer);
            KafkaTestUtils<CBSTransactionLogs> kafkaTestUtils = new KafkaTestUtils<CBSTransactionLogs>();
            try (var kafkaConsumer =
                         kafkaTestUtils.newConsumer(bootstrapServers, topic, groupId, cbsTranLogConsumer,
                                 CBSTransactionLogsDeserializer.class,dlqTopic,dlqPublisher)) {

                // publish message to cbs-gam topic
                kafkaTestUtils.publishMessages(bootstrapServers, topic, cbsTranLogEvent);

                // validate input messageEvent with messageEvent present in DLQ.
                DummyDlqConsumer<CBSTransactionLogs> dummyDlqConsumer = new DummyDlqConsumer<>();
                try (var dlqConsumer = kafkaTestUtils.newConsumer(
                        bootstrapServers, dlqTopic, groupId, dummyDlqConsumer, CBSTransactionLogsDeserializer.class)) {
                    await().atMost(MAX_WAIT_DURATION).until(() -> dummyDlqConsumer.totalMessages() >= 1);
                    assertThat(dummyDlqConsumer.messages).isEqualTo(List.of(cbsTranLogEvent));
                }
            }
        }
    }
    private Properties getDlqPublisherProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "com.kmbl.eventmanagementservice.service.streams.serializers.AvroSerializer");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "5000");
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        return props;
    }

    public static class DummyDlqConsumer<T> implements MessageConsumer<T> {

        private final List<T> messages = Collections.synchronizedList(new ArrayList<>());

        @Nullable
        @Override
        public String partitionKey(@Nullable T value) {
            return null;
        }

        @Override
        public void process(ReceiverRecord<String, T> message) {
            log.info("DummyDlqConsumer ...{}", message.value());
            var value = message.value();
            messages.add(value);
        }

        public int totalMessages() {
            return messages.size();
        }

        public T messageValue(int i) {
            return messages.get(i);
        }
    }
}