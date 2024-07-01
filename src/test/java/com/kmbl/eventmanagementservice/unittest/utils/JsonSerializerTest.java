package com.kmbl.eventmanagementservice.unittest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmbl.eventmanagementservice.utils.JsonSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonSerializerTest {

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private JsonSerializer<Person> jsonSerializer;

    @Test
    void serialize_SimpleObject_Success() throws Exception {
        // Arrange
        Person person = new Person("John Doe", 30);

        Mockito.when(mapper.writeValueAsBytes(person)).thenReturn(new byte[]{1, 2, 3, 4});

        // Act
        byte[] serializedData = jsonSerializer.serialize("test-topic", person);

        // Assert
        Assertions.assertArrayEquals(new byte[]{1, 2, 3, 4}, serializedData);
    }

    public static record Person(String name, int age) {}
}
