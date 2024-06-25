package com.kmbl.eventmanagementservice.service.streams;

import javax.annotation.Nullable;
import reactor.kafka.receiver.ReceiverRecord;

public interface MessageConsumer<T> {

    /**
     * Return the partition key used for assigning to an in-memory partition. If the returned value is
     *
     * @param value Message for which the partition key has to be returned
     * @return Partition key for this message. Can be null
     */
    @Nullable
    String partitionKey(@Nullable T value);

    void process(ReceiverRecord<String, T> message);
}
