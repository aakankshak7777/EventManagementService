package com.kmbl.eventmanagementservice.unittest.utils;




import com.kmbl.eventmanagementservice.utils.JsonDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonDeserializerTest {


    private JsonDeserializer<JsonSerializerTest.testObject> jsonDeserializer;
    @BeforeEach
    public void setUp() {
        jsonDeserializer = new JsonDeserializer<>(JsonSerializerTest.testObject.class);
    }
    @Test
    void deserialize_ValidData_Success() {
        byte[] data = "{\"name\":\"John Doe\",\"age\":30}".getBytes();
        jsonDeserializer = new JsonDeserializer<>(JsonSerializerTest.testObject.class);
        JsonSerializerTest.testObject testObject = new JsonSerializerTest.testObject("John Doe", 30);
        JsonSerializerTest.testObject deserializedTestObject = jsonDeserializer.deserialize("test-topic", data);
        Assertions.assertEquals(testObject, deserializedTestObject);
    }

    @Test
    void deserialize_NullData_ReturnsNull() {
        var deserializedTestObject = jsonDeserializer.deserialize("test-topic", null);
        Assertions.assertNull(deserializedTestObject);
    }
}
