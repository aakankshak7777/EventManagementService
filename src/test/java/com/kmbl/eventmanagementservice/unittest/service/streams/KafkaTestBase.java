package com.kmbl.eventmanagementservice.unittest.service.streams;

import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randInstant;
import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randStr;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

import com.kmbl.eventmanagementservice.Config.ContainerConfig;
import com.kmbl.eventmanagementservice.service.PartitionedEvent;
import com.kmbl.eventmanagementservice.testUtils.KafkaAdminUtils;
import com.kmbl.eventmanagementservice.utils.JsonDeserializer;
import java.time.Instant;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.With;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class KafkaTestBase {

    protected static String bootstrapServers;
    protected static final ContainerConfig containerConfig = new ContainerConfig();

    protected String topic;

    protected String groupId;

    @BeforeAll
    public static void setUpBeforeClass() {
        var container = containerConfig.kafkaContainer(false);
        bootstrapServers = container.getBootstrapServers();
    }

    @BeforeEach
    public void setUpBase() {

        this.topic = randStr("test-", 32);
        this.groupId = randStr("test-", 32);
        System.out.printf("Creating topic: %s\n", topic);
        KafkaAdminUtils.createTopic(bootstrapServers, this.topic);
    }

    @AfterEach
    public void cleanUpBase() {
        System.out.printf("Deleting topic: %s\n", this.topic);
        KafkaAdminUtils.deleteTopics(bootstrapServers, this.topic);
    }

    protected static List<CallbackEventLite> concat(List<List<CallbackEventLite>> lists) {
        return lists.stream().flatMap(Collection::stream).toList();
    }

    protected static List<CallbackEventLite> interleave(List<List<CallbackEventLite>> lists) {
        var prevTimestamp = randInstant();
        List<CallbackEventLite> result = new ArrayList<>();
        List<Iterator<CallbackEventLite>> iterators =
                lists.stream().map(List::iterator).toList();
        boolean atleastOneAdded;
        do {
            atleastOneAdded = false;
            for (var it : iterators) {
                if (it.hasNext()) {
                    var currTs = prevTimestamp.plusSeconds(10);
                    result.add(it.next().withEventTimestamp(currTs));
                    prevTimestamp = currTs;
                    atleastOneAdded = true;
                }
            }
        } while (atleastOneAdded);
        return result;
    }

    protected static List<List<CallbackEventLite>> generateForCallBackEvents(
            int totalAccounts, int totalTransactionsPerAccount) {
        var accountTxns = new ArrayList<List<CallbackEventLite>>(totalAccounts);
        var prevInstant = randInstant();
        for (int i = 1; i <= totalAccounts; ++i) {
            var callbackEvents = generate("txn" + i, "Debit",i, prevInstant);
            prevInstant = callbackEvents.get(callbackEvents.size() - 1).eventTimestamp;
            accountTxns.add(callbackEvents);
        }
        return accountTxns;
    }

    protected static List<CallbackEventLite> generate(
            String txnId, String type, int totalTransactions, Instant startingTimestamp) {
        var callbackEventLites = new ArrayList<CallbackEventLite>(totalTransactions);
        for (int i = 1; i <= totalTransactions; ++i) {
            if (callbackEventLites.isEmpty()) {
                callbackEventLites.add(new CallbackEventLite(txnId, type, startingTimestamp.plusSeconds(10)));
            } else {
                var prevInstant = callbackEventLites.get(callbackEventLites.size() - 1).eventTimestamp;
                callbackEventLites.add(new CallbackEventLite(txnId, type, prevInstant.plusSeconds(10)));
            }
        }
        return callbackEventLites;
    }

    protected Map<String, List<CallbackEventLite>> groupByAccount(List<CallbackEventLite> messages) {
        return messages.stream().collect(groupingBy(CallbackEventLite::getTransactionId));
    }

    protected Map<String, Set<CallbackEventLite>> groupByAccountAsSet(List<CallbackEventLite> messages) {
        return messages.stream().collect(groupingBy(CallbackEventLite::getTransactionId, toSet()));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @With
    public static class CallbackEventLite implements PartitionedEvent {

        public String transactionId;
        public String type;
        public Instant eventTimestamp;

        @Override
        public String partitionKey() {
            return transactionId;
        }

        @Override
        public String uniqueId() {
            return key();
        }

        public String key() {
            return String.join(":", transactionId, String.valueOf(type));
        }


    }

    public static class TransactionLiteDeserializer extends JsonDeserializer<CallbackEventLite> {

        public TransactionLiteDeserializer() {
            super(CallbackEventLite.class);
        }
    }

    @SneakyThrows
    public static void peacefulSleep(long millis) {
        Thread.sleep(millis);
    }
}
