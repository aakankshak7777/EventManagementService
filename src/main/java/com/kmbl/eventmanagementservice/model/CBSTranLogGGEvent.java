package com.kmbl.eventmanagementservice.model;

import com.kmbl.eventmanagementservice.service.PartitionedEvent;
import lombok.Builder;
import lombok.With;

@With
@Builder(toBuilder = true)
public record CBSTranLogGGEvent(
        Double id,
        String amount,
        String cbsrc,
        String creditAccount,
        String customerId,
        String debitAccount,
        String msgId,
        Long reversal,
        String reversalStatus,
        String rrn,
        String stan,
        String txnDate,
        String txnId,
        String type,
        String upirc,
        String orgTxnId,
        String remarks,
        String customerAccount,
        String name,
        String payeeVpa,
        String payerVpa,
        String seqNo,
        String commissionAmount,
        String customerRefId,
        String tranType,
        String creditBankName,
        String debitBankName,
        String othersAccount,
        String channel,
        String appId,
        String preApproved,
        String beneficiaryName,
        String otherMobileNo,
        String remitterName,
        String approvalNum,
        String mccCode,
        String serverIp,
        String cbsRespDate,
        String initMode,
        String purposeCode,
        String creditAccountType,
        String creditIfsc,
        String debitAccountType,
        String debitIfsc,
        String orderId,
        String payeeCode,
        String payerCode,
        String subType,
        String remittorMobileNumber,
        String payeeImei,
        String payerImei,
        String gmtDate,
        String payeeAccountType,
        String payerAccountType,
        String payerConsentName,
        String payerConsentType,
        String payerConsentValue,
        String adjCode,
        String adjFlag,
        String createDate,
        String creditAcctNarration103,
        String de46,
        String debitAcctNarration102,
        String gst,
        String loanNumber,
        String localTime,
        String narration125,
        String p2pTxnCharges,
        String schemeCode,
        String txnAmount,
        String batchId,
        String baseAmount,
        String baseCurr,
        String conCode,
        String fx,
        String mkup,
        String additional4,
        String d12,
        String additional1,
        String additional2,
        String additional3,
        String additional5,
        String cbsRouting
) implements PartitionedEvent {

    public static CBSTranLogGGEvent from(CBSTranLogData cbsTranLogData) {
        return CBSTranLogGGEvent.builder()
                .id(cbsTranLogData.id())
                .amount(cbsTranLogData.amount())
                .cbsrc(cbsTranLogData.cbsrc())
                .creditAccount(cbsTranLogData.creditAccount())
                .customerId(cbsTranLogData.customerId())
                .debitAccount(cbsTranLogData.debitAccount())
                .msgId(cbsTranLogData.msgId())
                .reversal(cbsTranLogData.reversal())
                .reversalStatus(cbsTranLogData.reversalStatus())
                .rrn(cbsTranLogData.rrn())
                .stan(cbsTranLogData.stan())
                .txnDate(cbsTranLogData.txnDate())
                .txnId(cbsTranLogData.txnId())
                .type(cbsTranLogData.type())
                .upirc(cbsTranLogData.upirc())
                .orgTxnId(cbsTranLogData.orgTxnId())
                .remarks(cbsTranLogData.remarks())
                .customerAccount(cbsTranLogData.customerAccount())
                .name(cbsTranLogData.name())
                .payeeVpa(cbsTranLogData.payeeVpa())
                .payerVpa(cbsTranLogData.payerVpa())
                .seqNo(cbsTranLogData.seqNo())
                .commissionAmount(cbsTranLogData.commissionAmount())
                .customerRefId(cbsTranLogData.customerRefId())
                .tranType(cbsTranLogData.tranType())
                .creditBankName(cbsTranLogData.creditBankName())
                .debitBankName(cbsTranLogData.debitBankName())
                .othersAccount(cbsTranLogData.othersAccount())
                .channel(cbsTranLogData.channel())
                .appId(cbsTranLogData.appId())
                .preApproved(cbsTranLogData.preApproved())
                .beneficiaryName(cbsTranLogData.beneficiaryName())
                .otherMobileNo(cbsTranLogData.otherMobileNo())
                .remitterName(cbsTranLogData.remitterName())
                .approvalNum(cbsTranLogData.approvalNum())
                .mccCode(cbsTranLogData.mccCode())
                .serverIp(cbsTranLogData.serverIp())
                .cbsRespDate(cbsTranLogData.cbsRespDate())
                .initMode(cbsTranLogData.initMode())
                .purposeCode(cbsTranLogData.purposeCode())
                .creditAccountType(cbsTranLogData.creditAccountType())
                .creditIfsc(cbsTranLogData.creditIfsc())
                .debitAccountType(cbsTranLogData.debitAccountType())
                .debitIfsc(cbsTranLogData.debitIfsc())
                .orderId(cbsTranLogData.orderId())
                .payeeCode(cbsTranLogData.payeeCode())
                .payerCode(cbsTranLogData.payerCode())
                .subType(cbsTranLogData.subType())
                .remittorMobileNumber(cbsTranLogData.remittorMobileNumber())
                .payeeImei(cbsTranLogData.payeeImei())
                .payerImei(cbsTranLogData.payerImei())
                .gmtDate(cbsTranLogData.gmtDate())
                .payeeAccountType(cbsTranLogData.payeeAccountType())
                .payerAccountType(cbsTranLogData.payerAccountType())
                .payerConsentName(cbsTranLogData.payerConsentName())
                .payerConsentType(cbsTranLogData.payerConsentType())
                .payerConsentValue(cbsTranLogData.payerConsentValue())
                .adjCode(cbsTranLogData.adjCode())
                .adjFlag(cbsTranLogData.adjFlag())
                .createDate(cbsTranLogData.createDate())
                .creditAcctNarration103(cbsTranLogData.creditAcctNarration103())
                .de46(cbsTranLogData.de46())
                .debitAcctNarration102(cbsTranLogData.debitAcctNarration102())
                .gst(cbsTranLogData.gst())
                .loanNumber(cbsTranLogData.loanNumber())
                .localTime(cbsTranLogData.localTime())
                .narration125(cbsTranLogData.narration125())
                .p2pTxnCharges(cbsTranLogData.p2pTxnCharges())
                .schemeCode(cbsTranLogData.schemeCode())
                .txnAmount(cbsTranLogData.txnAmount())
                .batchId(cbsTranLogData.batchId())
                .baseAmount(cbsTranLogData.baseAmount())
                .baseCurr(cbsTranLogData.baseCurr())
                .conCode(cbsTranLogData.conCode())
                .fx(cbsTranLogData.fx())
                .mkup(cbsTranLogData.mkup())
                .additional4(cbsTranLogData.additional4())
                .d12(cbsTranLogData.d12())
                .additional1(cbsTranLogData.additional1())
                .additional2(cbsTranLogData.additional2())
                .additional3(cbsTranLogData.additional3())
                .additional5(cbsTranLogData.additional5())
                .cbsRouting(cbsTranLogData.cbsRouting())
                .build();
    }


    @Override
    public String uniqueId() {
        return txnId;
    }

    @Override
    public String partitionKey() {
        return txnId;
    }
}
