package com.kmbl.eventmanagementservice.unittest.utils;

import com.kmbl.eventmanagementservice.utils.JsonSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonSerializerTest {


    @InjectMocks
    private JsonSerializer<testObject> jsonSerializer;

    @Test
    void serialize_SimpleObject_Success() throws Exception {

        testObject person = new testObject("John Doe", 30);
        byte[] serializedData = jsonSerializer.serialize("test-topic", person);
        Assertions.assertArrayEquals(new byte[]{123, 34, 110, 97, 109, 101, 34, 58, 34, 74, 111, 104, 110, 32, 68, 111, 101, 34, 44, 34, 97, 103, 101, 34, 58, 51, 48, 125}, serializedData);
    }

    public static record testObject(String name, int age) {}
}
