package com.kmbl.eventmanagementservice.dao.models;

import com.kmbl.eventmanagementservice.model.CBSTranLogData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbCBSTranLogData {

    private Double id;
    private String amount;
    private String cbsrc;
    private String creditAccount;
    private String customerId;
    private String debitAccount;
    private String msgId;
    private Long reversal;
    private String reversalStatus;
    private String rrn;
    private String stan;
    private String txnDate;
    private String txnId;
    private String type;
    private String upirc;
    private String orgTxnId;
    private String remarks;
    private String customerAccount;
    private String name;
    private String payeeVpa;
    private String payerVpa;
    private String seqNo;
    private String commissionAmount;
    private String customerRefId;
    private String tranType;
    private String creditBankName;
    private String debitBankName;
    private String othersAccount;
    private String channel;
    private String appId;
    private String preApproved;
    private String beneficiaryName;
    private String otherMobileNo;
    private String remitterName;
    private String approvalNum;
    private String mccCode;
    private String serverIp;
    private String cbsRespDate;
    private String initMode;
    private String purposeCode;
    private String creditAccountType;
    private String creditIfsc;
    private String debitAccountType;
    private String debitIfsc;
    private String orderId;
    private String payeeCode;
    private String payerCode;
    private String subType;
    private String remittorMobileNumber;
    private String payeeImei;
    private String payerImei;
    private String gmtDate;
    private String payeeAccountType;
    private String payerAccountType;
    private String payerConsentName;
    private String payerConsentType;
    private String payerConsentValue;
    private String adjCode;
    private String adjFlag;
    private String createDate;
    private String creditAcctNarration103;
    private String de46;
    private String debitAcctNarration102;
    private String gst;
    private String loanNumber;
    private String localTime;
    private String narration125;
    private String p2pTxnCharges;
    private String schemeCode;
    private String txnAmount;
    private String batchId;
    private String baseAmount;
    private String baseCurr;
    private String conCode;
    private String fx;
    private String mkup;
    private String additional4;
    private String d12;
    private String additional1;
    private String additional2;
    private String additional3;
    private String additional5;
    private String cbsRouting;

    public static DbCBSTranLogData from(CBSTranLogData cbstLog) {
        if (cbstLog == null) {
            return null;
        }
        return DbCBSTranLogData.builder()
                .id(cbstLog.id())
                .amount(cbstLog.amount())
                .cbsrc(cbstLog.cbsrc())
                .creditAccount(cbstLog.creditAccount())
                .customerId(cbstLog.customerId())
                .debitAccount(cbstLog.debitAccount())
                .msgId(cbstLog.msgId())
                .reversal(cbstLog.reversal())
                .reversalStatus(cbstLog.reversalStatus())
                .rrn(cbstLog.rrn())
                .stan(cbstLog.stan())
                .txnDate(cbstLog.txnDate())
                .txnId(cbstLog.txnId())
                .type(cbstLog.type())
                .upirc(cbstLog.upirc())
                .orgTxnId(cbstLog.orgTxnId())
                .remarks(cbstLog.remarks())
                .customerAccount(cbstLog.customerAccount())
                .name(cbstLog.name())
                .payeeVpa(cbstLog.payeeVpa())
                .payerVpa(cbstLog.payerVpa())
                .seqNo(cbstLog.seqNo())
                .commissionAmount(cbstLog.commissionAmount())
                .customerRefId(cbstLog.customerRefId())
                .tranType(cbstLog.tranType())
                .creditBankName(cbstLog.creditBankName())
                .debitBankName(cbstLog.debitBankName())
                .othersAccount(cbstLog.othersAccount())
                .channel(cbstLog.channel())
                .appId(cbstLog.appId())
                .preApproved(cbstLog.preApproved())
                .beneficiaryName(cbstLog.beneficiaryName())
                .otherMobileNo(cbstLog.otherMobileNo())
                .remitterName(cbstLog.remitterName())
                .approvalNum(cbstLog.approvalNum())
                .mccCode(cbstLog.mccCode())
                .serverIp(cbstLog.serverIp())
                .cbsRespDate(cbstLog.cbsRespDate())
                .initMode(cbstLog.initMode())
                .purposeCode(cbstLog.purposeCode())
                .creditAccountType(cbstLog.creditAccountType())
                .creditIfsc(cbstLog.creditIfsc())
                .debitAccountType(cbstLog.debitAccountType())
                .debitIfsc(cbstLog.debitIfsc())
                .orderId(cbstLog.orderId())
                .payeeCode(cbstLog.payeeCode())
                .payerCode(cbstLog.payerCode())
                .subType(cbstLog.subType())
                .remittorMobileNumber(cbstLog.remittorMobileNumber())
                .payeeImei(cbstLog.payeeImei())
                .payerImei(cbstLog.payerImei())
                .gmtDate(cbstLog.gmtDate())
                .payeeAccountType(cbstLog.payeeAccountType())
                .payerAccountType(cbstLog.payerAccountType())
                .payerConsentName(cbstLog.payerConsentName())
                .payerConsentType(cbstLog.payerConsentType())
                .payerConsentValue(cbstLog.payerConsentValue())
                .adjCode(cbstLog.adjCode())
                .adjFlag(cbstLog.adjFlag())
                .createDate(cbstLog.createDate())
                .creditAcctNarration103(cbstLog.creditAcctNarration103())
                .de46(cbstLog.de46())
                .debitAcctNarration102(cbstLog.debitAcctNarration102())
                .gst(cbstLog.gst())
                .loanNumber(cbstLog.loanNumber())
                .localTime(cbstLog.localTime())
                .narration125(cbstLog.narration125())
                .p2pTxnCharges(cbstLog.p2pTxnCharges())
                .schemeCode(cbstLog.schemeCode())
                .txnAmount(cbstLog.txnAmount())
                .batchId(cbstLog.batchId())
                .baseAmount(cbstLog.baseAmount())
                .baseCurr(cbstLog.baseCurr())
                .conCode(cbstLog.conCode())
                .fx(cbstLog.fx())
                .mkup(cbstLog.mkup())
                .additional4(cbstLog.additional4())
                .d12(cbstLog.d12())
                .additional1(cbstLog.additional1())
                .additional2(cbstLog.additional2())
                .additional3(cbstLog.additional3())
                .additional5(cbstLog.additional5())
                .cbsRouting(cbstLog.cbsRouting())
                .build();
    }

    public CBSTranLogData toCBSTranLogData() {
        return CBSTranLogData.builder()
                .id(id)
                .amount(amount)
                .cbsrc(cbsrc)
                .creditAccount(creditAccount)
                .customerId(customerId)
                .debitAccount(debitAccount)
                .msgId(msgId)
                .reversal(reversal)
                .reversalStatus(reversalStatus)
                .rrn(rrn)
                .stan(stan)
                .txnDate(txnDate)
                .txnId(txnId)
                .type(type)
                .upirc(upirc)
                .orgTxnId(orgTxnId)
                .remarks(remarks)
                .customerAccount(customerAccount)
                .name(name)
                .payeeVpa(payeeVpa)
                .payerVpa(payerVpa)
                .seqNo(seqNo)
                .commissionAmount(commissionAmount)
                .customerRefId(customerRefId)
                .tranType(tranType)
                .creditBankName(creditBankName)
                .debitBankName(debitBankName)
                .othersAccount(othersAccount)
                .channel(channel)
                .appId(appId)
                .preApproved(preApproved)
                .beneficiaryName(beneficiaryName)
                .otherMobileNo(otherMobileNo)
                .remitterName(remitterName)
                .approvalNum(approvalNum)
                .mccCode(mccCode)
                .serverIp(serverIp)
                .cbsRespDate(cbsRespDate)
                .initMode(initMode)
                .purposeCode(purposeCode)
                .creditAccountType(creditAccountType)
                .creditIfsc(creditIfsc)
                .debitAccountType(debitAccountType)
                .debitIfsc(debitIfsc)
                .orderId(orderId)
                .payeeCode(payeeCode)
                .payerCode(payerCode)
                .subType(subType)
                .remittorMobileNumber(remittorMobileNumber)
                .payeeImei(payeeImei)
                .payerImei(payerImei)
                .gmtDate(gmtDate)
                .payeeAccountType(payeeAccountType)
                .payerAccountType(payerAccountType)
                .payerConsentName(payerConsentName)
                .payerConsentType(payerConsentType)
                .payerConsentValue(payerConsentValue)
                .adjCode(adjCode)
                .adjFlag(adjFlag)
                .createDate(createDate)
                .creditAcctNarration103(creditAcctNarration103)
                .de46(de46)
                .debitAcctNarration102(debitAcctNarration102)
                .gst(gst)
                .loanNumber(loanNumber)
                .localTime(localTime)
                .narration125(narration125)
                .p2pTxnCharges(p2pTxnCharges)
                .schemeCode(schemeCode)
                .txnAmount(txnAmount)
                .batchId(batchId)
                .baseAmount(baseAmount)
                .baseCurr(baseCurr)
                .conCode(conCode)
                .fx(fx)
                .mkup(mkup)
                .additional4(additional4)
                .d12(d12)
                .additional1(additional1)
                .additional2(additional2)
                .additional3(additional3)
                .additional5(additional5)
                .cbsRouting(cbsRouting)
                .build();
    }
}
