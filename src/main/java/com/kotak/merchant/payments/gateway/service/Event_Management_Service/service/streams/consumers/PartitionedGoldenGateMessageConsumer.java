package com.kotak.merchant.payments.gateway.service.Event_Management_Service.service.streams.consumers;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.utils.EventsUtil;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import reactor.kafka.receiver.ReceiverRecord;

@RequiredArgsConstructor
public abstract class PartitionedGoldenGateMessageConsumer<T, V> implements MessageConsumer<T> {

    private final Function<T, V> before;

    private final Function<T, V> after;

    private final Function<V, ByteBuffer> accountId;

    private final Consumer<ReceiverRecord<String, T>> consumer;

    @Nullable
    @Override
    public String partitionKey(@Nullable T value) {
        if (value == null) {
            return null;
        }
        var vafter = after.apply(value);
        var bbaccountId = vafter == null ? null : accountId.apply(vafter);
        if (bbaccountId == null) {
            var vbefore = before.apply(value);
            bbaccountId = vbefore == null ? null : accountId.apply(vbefore);
        }
        return bbaccountId == null ? null : EventsUtil.byteBuffToStr(bbaccountId);
    }

    @Override
    public void process(ReceiverRecord<String, T> message) {
        consumer.accept(message);
    }
}
