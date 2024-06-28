 package com.kmbl.eventmanagementservice.service.streams.consumers;


 import com.kmbl.eventmanagementservice.service.streams.consumerservice.ConsumerService;
 import com.kmbl.eventmanagementservice.Schema.CBSTranCol;
 import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
 import lombok.extern.slf4j.Slf4j;

 @Slf4j
 public class CBSTranLogConsumer extends PartitionedGoldenGateMessageConsumer<CBSTransactionLogs, CBSTranCol> {

    public CBSTranLogConsumer(ConsumerService svc) {

        super(CBSTransactionLogs::getBefore, CBSTransactionLogs::getAfter, CBSTranCol::getTXNID, m -> {
            svc.sendMessage(m.value());
        });
    }
 }
