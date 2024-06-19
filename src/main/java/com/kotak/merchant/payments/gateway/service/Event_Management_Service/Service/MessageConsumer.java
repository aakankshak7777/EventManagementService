package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service;

import javax.annotation.Nullable;
import reactor.kafka.receiver.ReceiverRecord;

public interface MessageConsumer<T> {

    /**
     * Return the partition key used for assigning to an in-memory partition. If the returned value is
     * null, the message is assigned to a random in-memory partition.
     *
     * @param value Message for which the partition key has to be returned
     * @return Partition key for this message. Can be null
     */
    @Nullable
    String partitionKey(@Nullable T value);

    void process(ReceiverRecord<String, T> message);
}
