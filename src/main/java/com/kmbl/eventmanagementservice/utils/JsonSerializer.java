package com.kmbl.eventmanagementservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerializer<T> implements Serializer<T> {

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Override
    public byte[] serialize(String topic, T data) {

        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Serialization failed for: " + data, e);
        }
    }
}
