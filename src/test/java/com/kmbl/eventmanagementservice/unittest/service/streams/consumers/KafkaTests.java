package com.kmbl.eventmanagementservice.unittest.service.streams.consumers;


import com.kmbl.eventmanagementservice.Config.ContainerConfig;
import com.kmbl.eventmanagementservice.dao.CBSTranLogDao;
import com.kmbl.eventmanagementservice.dao.DdbCBSTranLogDao;
import com.kmbl.eventmanagementservice.service.streams.consumers.CBSTranLogConsumer;
import com.kmbl.eventmanagementservice.service.streams.consumerservice.ConsumerService;
import com.kmbl.eventmanagementservice.testUtils.KafkaAdminUtils;
import com.kmbl.eventmanagementservice.unittest.DynamoDbSetupBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randEpoch;
import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randStr;

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
    private CBSTranLogDao dao;

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
        dao = new DdbCBSTranLogDao(ddb);
    }

    @AfterEach
    public void cleanUpBase() {
        System.out.printf("Deleting topic: %s\n", this.topic);
        KafkaAdminUtils.deleteTopics(bootstrapServers, this.topic);
    }
//
//    @Test
//    public void CbsTranLogConsumerToDDB_insertEvent_insertedEventDataInDDB() throws InterruptedException {
//          consumerService = new ConsumerService(new CBSTranLogService(dao));
//        this.cbsTranLogConsumer = new CBSTranLogConsumer(consumerService);
//        // form data for publishing and expected data in DDB.
//        var gamEvent = UnitDataGenUtils.getInsertDataEvent();
//        KafkaTestUtils<CBSTransactionLogs> kafkaTestUtils = new KafkaTestUtils<CBSTransactionLogs>();
//        try (var kafkaConsumer =
//                     kafkaTestUtils.newConsumer(bootstrapServers, topic, groupId, cbsTranLogConsumer,
//                             CBSTransactionLogsDeserializer.class)) {
//            kafkaTestUtils.publishMessages(bootstrapServers, topic, gamEvent);
//            Thread.sleep(1000);
//
//        }
//    }
}
