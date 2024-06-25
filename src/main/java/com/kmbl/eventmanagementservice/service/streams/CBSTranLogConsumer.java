 package com.kmbl.eventmanagementservice.service.streams;


 import com.kmbl.eventmanagementservice.service.streams.consumers.PartitionedGoldenGateMessageConsumer;
 import com.kmbl.eventmanagementservice.Schema.CBSTranCol;
 import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
 import lombok.extern.slf4j.Slf4j;

 @Slf4j
 public class CBSTranLogConsumer extends PartitionedGoldenGateMessageConsumer<CBSTransactionLogs, CBSTranCol> {

    public CBSTranLogConsumer(ConsumerService svc) {

        super(CBSTransactionLogs::getBefore, CBSTransactionLogs::getAfter, CBSTranCol::getTXNID, m -> {
            if (svc.printMessage(m.value())) {
            }
        });
    }
 }
