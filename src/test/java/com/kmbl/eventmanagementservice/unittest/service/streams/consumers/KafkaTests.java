package com.kmbl.eventmanagementservice.unittest.service.streams.consumers;



import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randEpoch;
import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randStr;

import com.kmbl.eventmanagementservice.Config.ContainerConfig;
import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import com.kmbl.eventmanagementservice.dao.CBSTranLogGGDao;
import com.kmbl.eventmanagementservice.dao.DdbCBSTranLogGGDao;
import com.kmbl.eventmanagementservice.service.CBSTranLogGGService;
import com.kmbl.eventmanagementservice.service.event.CBSTranLogGGEventService;
import com.kmbl.eventmanagementservice.service.streams.consumers.CBSTranLogConsumer;
import com.kmbl.eventmanagementservice.service.streams.consumerservice.ConsumerService;
import com.kmbl.eventmanagementservice.service.streams.serializers.CBSTransactionLogsDeserializer;
import com.kmbl.eventmanagementservice.testUtils.KafkaAdminUtils;
import com.kmbl.eventmanagementservice.testUtils.KafkaTestUtils;
import com.kmbl.eventmanagementservice.testUtils.UnitDataGenUtils;
import com.kmbl.eventmanagementservice.unittest.DynamoDbSetupBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KafkaTests extends DynamoDbSetupBase
{
    private static final ContainerConfig containerConfig = new ContainerConfig();
    private static String bootstrapServers;
    private String topic;
    protected String groupId;
    private CBSTranLogConsumer cbsTranLogConsumer;
    private long currEpoch;
    private ConsumerService consumerService;
    private CBSTranLogGGDao dao;

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
        dao = new DdbCBSTranLogGGDao(ddb);
    }

    @AfterEach
    public void cleanUpBase() {
        System.out.printf("Deleting topic: %s\n", this.topic);
        KafkaAdminUtils.deleteTopics(bootstrapServers, this.topic);
    }

    @Test
    public void CbsTranLogConsumerToDDB_insertEvent_insertedEventDataInDDB() throws InterruptedException {
          consumerService = new ConsumerService(new CBSTranLogGGService(dao),new CBSTranLogGGEventService());
        this.cbsTranLogConsumer = new CBSTranLogConsumer(consumerService);
        // form data for publishing and expected data in DDB.
        var gamEvent = UnitDataGenUtils.getInsertDataEvent();
        KafkaTestUtils<CBSTransactionLogs> kafkaTestUtils = new KafkaTestUtils<CBSTransactionLogs>();
        try (var kafkaConsumer =
                     kafkaTestUtils.newConsumer(bootstrapServers, topic, groupId, cbsTranLogConsumer,
                             CBSTransactionLogsDeserializer.class)) {
            kafkaTestUtils.publishMessages(bootstrapServers, topic, gamEvent);
            Thread.sleep(1000);

        }
    }


}
