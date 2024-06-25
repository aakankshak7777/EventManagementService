package com.kmbl.eventmanagementservice.unittests.service.streams;



import static com.kmbl.eventmanagementservice.testutils.RandUtils.randEpoch;
import static com.kmbl.eventmanagementservice.testutils.RandUtils.randStr;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import com.kmbl.eventmanagementservice.config.ContainerConfig;
import com.kmbl.eventmanagementservice.service.streams.CBSTranLogConsumer;
import com.kmbl.eventmanagementservice.service.streams.ConsumerService;
import com.kmbl.eventmanagementservice.service.streams.serializers.CBSTransactionLogsDeserializers;
import com.kmbl.eventmanagementservice.testutils.KafkaAdminUtils;
import com.kmbl.eventmanagementservice.testutils.KafkaTestUtils;
import com.kmbl.eventmanagementservice.testutils.UnitDataGenUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KafkaTests
{
    private static final ContainerConfig containerConfig = new ContainerConfig();
    private static String bootstrapServers;
    private String topic;
    protected String groupId;
    private CBSTranLogConsumer cbsTranLogConsumer;
    private long currEpoch;
    private ConsumerService consumerService;

    @BeforeAll
    public static void setUpBeforeClass() {

        var container = containerConfig.kafkaContainer(false);
        bootstrapServers = container.getBootstrapServers();
    }

    @BeforeEach
    public void setUp() {
        this.topic = randStr("test-", 32);
        this.groupId = randStr("test-", 32);
        System.out.printf("Creating topic: %s\n", topic);
        KafkaAdminUtils.createTopic(bootstrapServers, this.topic);
        currEpoch = randEpoch();
    }

    @AfterEach
    public void cleanUpBase() {
        System.out.printf("Deleting topic: %s\n", this.topic);
        KafkaAdminUtils.deleteTopics(bootstrapServers, this.topic);
    }

    @Test
    public void CbsTranLogConsumerToDDB_insertEvent_insertedEventDataInDDB() throws InterruptedException {
          consumerService = new ConsumerService();
        this.cbsTranLogConsumer = new CBSTranLogConsumer(consumerService);
        // form data for publishing and expected data in DDB.
        var gamEvent = UnitDataGenUtils.getCBSInsertDataEvent();
        KafkaTestUtils<CBSTransactionLogs> kafkaTestUtils = new KafkaTestUtils<CBSTransactionLogs>();
        try (var kafkaConsumer =
                     kafkaTestUtils.newConsumer(bootstrapServers, topic, groupId, cbsTranLogConsumer, CBSTransactionLogsDeserializers.class)) {
            kafkaTestUtils.publishMessages(bootstrapServers, topic, gamEvent);
            Thread.sleep(10000);

        }
    }


}
