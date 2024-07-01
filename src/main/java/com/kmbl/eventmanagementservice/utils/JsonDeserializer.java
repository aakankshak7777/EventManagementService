package com.kmbl.eventmanagementservice.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class JsonDeserializer<T> implements Deserializer<T> {

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    private final Class<T> clazz;

    public JsonDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return mapper.readValue(data, clazz);
        } catch (IOException e) {
            throw new SerializationException("Error when deserializing byte[] to " + clazz.getName(), e);
        }
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        try {
            // TODO: remove this log post testing
            log.debug("Inside JsonDeserializer ...");
            return deserialize(topic, data);
        } catch (SerializationException e) {
            return null;
        }
    }
}
