package com.kmbl.eventmanagementservice.service.streams.serializers;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.codec.binary.Hex;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class AvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

    private final List<Schema> writerSchemas;
    private final Schema readerSchema;

    public AvroDeserializer(List<Schema> writerSchemas, Schema readerSchema) {
        this.writerSchemas = writerSchemas;
        this.readerSchema = readerSchema;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        var event = new String(Hex.encodeHex(data));
        for (Schema schema : writerSchemas) {
            try {
                var decoder = DecoderFactory.get().binaryDecoder(data, null);
                return new SpecificDatumReader<T>(schema, readerSchema).read(null, decoder);
            } catch (Exception e) {
                log.error("Deserialization failure for event {} into schema {}", event, readerSchema.getName(), e);
            }
        }
        throw new SerializationException("Error when deserializing byte[] to " + readerSchema.getName());
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        String eventString = new String(Hex.encodeHex(data));
        log.debug("AvroDeserializer:deserialize received encodeHex {} with headers {}", eventString, headers);
        try {
            return deserialize(topic, data);
        } catch (SerializationException e) {
            log.error("Failed to deserialize eventString: {}", eventString, e);
            throw e;
        }
    }
}
