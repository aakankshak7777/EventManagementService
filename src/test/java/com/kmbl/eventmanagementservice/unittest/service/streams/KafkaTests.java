package com.kmbl.eventmanagementservice.unittest.service.streams;



import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randEpoch;
import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randStr;
import static com.kmbl.eventmanagementservice.testUtils.UnitDataGenUtils.randCollectCallbackEvent;
import static com.kmbl.eventmanagementservice.testUtils.UnitDataGenUtils.randCredit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.kmbl.eventmanagementservice.Config.ContainerConfig;
import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.model.CollectCallbackEvent;
import com.kmbl.eventmanagementservice.service.CollectorCallbackEventService;
import com.kmbl.eventmanagementservice.service.streams.consumers.CBSTranLogConsumer;
import com.kmbl.eventmanagementservice.service.streams.consumerservice.ConsumerService;
import com.kmbl.eventmanagementservice.service.streams.producers.KafkaPublisher;
import com.kmbl.eventmanagementservice.service.streams.serializers.CBSTransactionLogsDeserializers;
import com.kmbl.eventmanagementservice.testUtils.KafkaAdminUtils;
import com.kmbl.eventmanagementservice.testUtils.KafkaTestUtils;
import com.kmbl.eventmanagementservice.testUtils.UnitDataGenUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

    private CollectorCallbackEventService svc;

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

//    @Test
//    public void TranLogPublisherToKafka_InsertEvent_PublishEventToATopic() throws InterruptedException {
//        var collectCallbackEvent = randCollectCallbackEvent(EventName.COLLECT_CALLBACK_API).;
//        when(kpublisher.offer(collectCallbackEvent)).thenReturn(true);
//        assertThat(svc.queueUp(collectCallbackEvent)).isTrue();
//    }


}
