package com.kmbl.eventmanagementservice.service.dtos.requests;

import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.enums.EventStatus;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLog;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogData;
import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogEvent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record CBSTranLogRequest(
        @NotNull String transactionId,
        @NotNull String type,
        CBSTranLogData metaData,
        EventName eventName) {

    public static CBSTranLogRequest from(CBSTranLogData cbsTranLogData, EventName eventName) {
        return CBSTranLogRequest.builder()
                .transactionId(cbsTranLogData.txnId())
                .type(cbsTranLogData.type())
                .metaData(cbsTranLogData)
                .eventName(eventName)
                .build();
    }

    public CBSTranLog toCBSTranLog(EventName eventName, EventStatus eventStatus) {
        return CBSTranLog.builder()
                .transactionId(transactionId)
                .type(type)
                .metaData(metaData)
                .eventStatus(eventStatus)
                .createdBy(eventName)
                .build();
    }

    public CBSTranLogEvent CBSTranLogEvent(EventName eventName) {
        if (metaData == null) {
            return null;
        }
        return CBSTranLogEvent.builder()
                .id(metaData.id())
                .amount(metaData.amount())
                .cbsrc(metaData.cbsrc())
                .creditAccount(metaData.creditAccount())
                .customerId(metaData.customerId())
                .debitAccount(metaData.debitAccount())
                .msgId(metaData.msgId())
                .reversal(metaData.reversal())
                .reversalStatus(metaData.reversalStatus())
                .rrn(metaData.rrn())
                .stan(metaData.stan())
                .txnDate(metaData.txnDate())
                .txnId(transactionId())
                .type(type())
                .upirc(metaData.upirc())
                .orgTxnId(metaData.orgTxnId())
                .remarks(metaData.remarks())
                .customerAccount(metaData.customerAccount())
                .name(metaData.name())
                .payeeVpa(metaData.payeeVpa())
                .payerVpa(metaData.payerVpa())
                .seqNo(metaData.seqNo())
                .commissionAmount(metaData.commissionAmount())
                .customerRefId(metaData.customerRefId())
                .tranType(metaData.tranType())
                .creditBankName(metaData.creditBankName())
                .debitBankName(metaData.debitBankName())
                .othersAccount(metaData.othersAccount())
                .channel(metaData.channel())
                .appId(metaData.appId())
                .preApproved(metaData.preApproved())
                .beneficiaryName(metaData.beneficiaryName())
                .otherMobileNo(metaData.otherMobileNo())
                .remitterName(metaData.remitterName())
                .approvalNum(metaData.approvalNum())
                .mccCode(metaData.mccCode())
                .serverIp(metaData.serverIp())
                .cbsRespDate(metaData.cbsRespDate())
                .initMode(metaData.initMode())
                .purposeCode(metaData.purposeCode())
                .creditAccountType(metaData.creditAccountType())
                .creditIfsc(metaData.creditIfsc())
                .debitAccountType(metaData.debitAccountType())
                .debitIfsc(metaData.debitIfsc())
                .orderId(metaData.orderId())
                .payeeCode(metaData.payeeCode())
                .payerCode(metaData.payerCode())
                .subType(metaData.subType())
                .remittorMobileNumber(metaData.remittorMobileNumber())
                .payeeImei(metaData.payeeImei())
                .payerImei(metaData.payerImei())
                .gmtDate(metaData.gmtDate())
                .payeeAccountType(metaData.payeeAccountType())
                .payerAccountType(metaData.payerAccountType())
                .payerConsentName(metaData.payerConsentName())
                .payerConsentType(metaData.payerConsentType())
                .payerConsentValue(metaData.payerConsentValue())
                .adjCode(metaData.adjCode())
                .adjFlag(metaData.adjFlag())
                .createDate(metaData.createDate())
                .creditAcctNarration103(metaData.creditAcctNarration103())
                .de46(metaData.de46())
                .debitAcctNarration102(metaData.debitAcctNarration102())
                .gst(metaData.gst())
                .loanNumber(metaData.loanNumber())
                .localTime(metaData.localTime())
                .narration125(metaData.narration125())
                .p2pTxnCharges(metaData.p2pTxnCharges())
                .schemeCode(metaData.schemeCode())
                .txnAmount(metaData.txnAmount())
                .batchId(metaData.batchId())
                .baseAmount(metaData.baseAmount())
                .baseCurr(metaData.baseCurr())
                .conCode(metaData.conCode())
                .fx(metaData.fx())
                .mkup(metaData.mkup())
                .additional4(metaData.additional4())
                .d12(metaData.d12())
                .additional1(metaData.additional1())
                .additional2(metaData.additional2())
                .additional3(metaData.additional3())
                .additional5(metaData.additional5())
                .cbsRouting(metaData.cbsRouting())
                .build();
    }

    private Object nullCheck() {
        return null;
    }
}