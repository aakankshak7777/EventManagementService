package com.kmbl.eventmanagementservice.utils;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class AvroSerializerUtil <T> implements Serializer<T> {

    @Override
    public byte[] serialize(String topic, T data) {

        try {
            DatumWriter<T> datumWriter = new SpecificDatumWriter<>(getClassSchema(data));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            datumWriter.write(data, encoder);
            encoder.flush();
            outputStream.close();

            byte[] bytedata = outputStream.toByteArray();
            return bytedata;
        } catch (IOException e) {
            throw new SerializationException("Serialization failed for: " + data, e);
        }
    }

    private Schema getClassSchema(T data)
    {
            return CBSTransactionLogs.getClassSchema();
    }
}
