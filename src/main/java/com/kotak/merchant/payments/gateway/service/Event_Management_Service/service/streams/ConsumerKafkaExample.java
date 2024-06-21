package com.kotak.merchant.payments.gateway.service.Event_Management_Service.service.streams;

import com.kmbl.realtimetransactionservice.event.schema.RtsCbsGamEvents.GamColumns;
import com.kmbl.realtimetransactionservice.event.schema.RtsCbsGamEvents.GeneralAcctMastTable;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.service.streams.consumers.PartitionedGoldenGateMessageConsumer;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.utils.EventsUtil;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerKafkaExample extends PartitionedGoldenGateMessageConsumer<GeneralAcctMastTable, GamColumns> {

    private static final Function<GeneralAcctMastTable, Optional<Instant>> tsFunc = EventsUtil.generateTimestampFunc(
            GeneralAcctMastTable::getBefore,
            GeneralAcctMastTable::getAfter,
            GamColumns::getLCHGTIME,
            GeneralAcctMastTable::getOpTs);

    public ConsumerKafkaExample(ServiceExample svc) {
        super(GeneralAcctMastTable::getBefore, GeneralAcctMastTable::getAfter, GamColumns::getACID, m -> {
            if (svc.printMessage(m.value())) {
                var currInstant = Instant.now();
                var eventTs = tsFunc.apply(m.value());
                var lag = eventTs.map(ets -> Duration.between(ets, currInstant));
                log.info("Updated GAM record with lag: {}", lag);
            }
        });
    }
}
