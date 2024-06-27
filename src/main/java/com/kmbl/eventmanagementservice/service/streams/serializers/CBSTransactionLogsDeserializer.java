package com.kmbl.eventmanagementservice.service.streams.serializers;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import java.util.List;

public class CBSTransactionLogsDeserializer extends AvroDeserializer<CBSTransactionLogs> {

    public CBSTransactionLogsDeserializer() {
        super(
                List.of(
                        CBSTransactionLogs.getClassSchema()),
                CBSTransactionLogs.getClassSchema());
    }
}
