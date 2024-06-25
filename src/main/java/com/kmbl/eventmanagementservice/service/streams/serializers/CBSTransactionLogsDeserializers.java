package com.kmbl.eventmanagementservice.service.streams.serializers;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import java.util.List;

public class CBSTransactionLogsDeserializers extends AvroDeserializer<CBSTransactionLogs> {

    public CBSTransactionLogsDeserializers() {
        super(
                List.of(
                        CBSTransactionLogs.getClassSchema()),
                CBSTransactionLogs.getClassSchema());
    }
}
