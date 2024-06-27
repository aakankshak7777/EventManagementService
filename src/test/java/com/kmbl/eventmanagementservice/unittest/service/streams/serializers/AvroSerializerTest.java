package com.kmbl.eventmanagementservice.unittest.service.streams.serializers;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import com.kmbl.eventmanagementservice.service.streams.serializers.AvroSerializer;
import com.kmbl.eventmanagementservice.testUtils.UnitDataGenUtils;
import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class AvroSerializerTest {
    @InjectMocks
    private AvroSerializer<CBSTransactionLogs> avroSerializer;
    private String topic;

    @Mock
    private CBSTransactionLogs cbsTransactionLogs;
    @BeforeEach
    public void setUp() {
        avroSerializer = new AvroSerializer<>();
        String topic = "test-topic";
    }
    @Test
    void testSerialize() {
        var data = UnitDataGenUtils.getInsertDataEvent();

        byte[] serializedData = avroSerializer.serialize(topic, data);
        Assertions.assertNotNull(serializedData);
        Assertions.assertTrue(serializedData.length > 0);
    }

    @Test
    void testSerializeWithException() {
        Mockito.when(cbsTransactionLogs.getSchema()).thenThrow(new SerializationException("Test exception"));
        Assertions.assertThrows(
                SerializationException.class,
                () -> avroSerializer.serialize(topic, cbsTransactionLogs)
        );
    }


}