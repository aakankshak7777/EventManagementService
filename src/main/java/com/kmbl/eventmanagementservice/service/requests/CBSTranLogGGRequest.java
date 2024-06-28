package com.kmbl.eventmanagementservice.service.requests;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.model.CBSTranLogData;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogGG;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record CBSTranLogGGRequest(
        @NotNull String transactionId,
        @NotNull String type,
        CBSTranLogData metaData,
        EventName eventName) {

    public static CBSTranLogGGRequest from(CBSTranLogData cbsTranLogData, EventName eventName) {
        return CBSTranLogGGRequest.builder()
                .transactionId(cbsTranLogData.txnId())
                .type(cbsTranLogData.type())
                .metaData(cbsTranLogData)
                .eventName(eventName)
                .build();
    }

    public CBSTranLogGG toCBSTranLog(EventStatus eventStatus) {
        return CBSTranLogGG.builder()
                .transactionId(transactionId)
                .type(type)
                .metaData(metaData)
                .eventStatus(eventStatus)
                .createdBy(eventName)
                .build();
    }
}