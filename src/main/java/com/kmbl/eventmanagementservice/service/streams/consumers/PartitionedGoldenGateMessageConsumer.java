package com.kmbl.eventmanagementservice.service.streams.consumers;

import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import reactor.kafka.receiver.ReceiverRecord;

@RequiredArgsConstructor
public abstract class PartitionedGoldenGateMessageConsumer<T, V> implements MessageConsumer<T> {

    private final Function<T, V> before;

    private final Function<T, V> after;

    private final Function<V, CharSequence> txnId;

    private final Consumer<ReceiverRecord<String, T>> consumer;

    @Nullable
    @Override
    public String partitionKey(@Nullable T value) {
        if (value == null) {
            return null;
        }
        var vafter = after.apply(value);
        var tranId = vafter == null ? null : txnId.apply(vafter);
        if (tranId == null) {
            var vbefore = before.apply(value);
            tranId = vbefore == null ? null : txnId.apply(vbefore);
        }
        return String.valueOf(tranId);
    }

    @Override
    public void process(ReceiverRecord<String, T> message) {
        consumer.accept(message);
    }
}
