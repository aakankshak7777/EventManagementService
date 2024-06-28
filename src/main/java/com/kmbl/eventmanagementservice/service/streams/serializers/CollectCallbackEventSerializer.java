package com.kmbl.eventmanagementservice.service.streams.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmbl.eventmanagementservice.model.CollectCallbackEvent;
import java.io.IOException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;


public class CollectCallbackEventSerializer implements Serializer<CollectCallbackEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    @Override
    public byte[] serialize(String topic, CollectCallbackEvent data) {
        try {
            String json = objectMapper.writeValueAsString(data);
            return json.getBytes();
        } catch (IOException e) {
            throw new SerializationException("Serialization failed for: " + data, e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, CollectCallbackEvent data) {
        return Serializer.super.serialize(topic, headers, data);
    }

}