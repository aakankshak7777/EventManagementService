package com.kotak.merchant.payments.gateway.service.Event_Management_Service.utils.mdc;

import java.io.Closeable;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class MdcPublisher implements Closeable, AutoCloseable {

    private static final String TOPIC = "topic";
    private static final String MKEY = "mkey";

    public MdcPublisher(KafkaMdc mdc) {
        if (mdc.key() != null) {
            MDC.put(MKEY, mdc.key());
        }
        if (mdc.topic() != null) {
            MDC.put(TOPIC, mdc.topic());
        }
    }

    public void addMkey(String key) {
        MDC.put(MKEY, key);
    }

    @Override
    public void close() {

        MDC.remove(MKEY);
        MDC.remove(TOPIC);
    }
}
