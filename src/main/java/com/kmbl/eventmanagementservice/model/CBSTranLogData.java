package com.kmbl.eventmanagementservice.model;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import lombok.Builder;

@Builder(toBuilder = true)
public record CBSTranLogData(
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
        )
{

        public static CBSTranLogData to(CBSTransactionLogs cbsTransactionLogs) {
                var data = cbsTransactionLogs.getAfter();
                return CBSTranLogData.builder()
                        .id(data.getID())
                        .amount(handleNull(data.getAMOUNT()))
                        .cbsrc(handleNull(data.getCBSRC()))
                        .creditAccount(handleNull(data.getCREDITACCOUNT()))
                        .customerId(handleNull(data.getCUSTOMERID()))
                        .debitAccount(handleNull(data.getDEBITACCOUNT()))
                        .msgId(handleNull(data.getMSGID()))
                        .reversal(data.getREVERSAL())
                        .reversalStatus(handleNull(data.getREVERSALSTATUS()))
                        .rrn(handleNull(data.getRRN()))
                        .stan(handleNull(data.getSTAN()))
                        .txnDate(handleNull(data.getTXNDATE()))
                        .txnId(handleNull(data.getTXNID()))
                        .type(handleNull(data.getTYPE()))
                        .upirc(handleNull(data.getUPIRC()))
                        .orgTxnId(handleNull(data.getORGTXNID()))
                        .remarks(handleNull(data.getREMARKS()))
                        .customerAccount(handleNull(data.getCUSTOMERACCOUNT()))
                        .name(handleNull(data.getNAME()))
                        .payeeVpa(handleNull(data.getPAYEEVPA()))
                        .payerVpa(handleNull(data.getPAYERVPA()))
                        .seqNo(handleNull(data.getSEQNO()))
                        .commissionAmount(handleNull(data.getCOMMISSIONAMOUNT()))
                        .customerRefId(handleNull(data.getCUSTOMERREFID()))
                        .tranType(handleNull(data.getTRANTYPE()))
                        .creditBankName(handleNull(data.getCREDITBANKNAME()))
                        .debitBankName(handleNull(data.getDEBITBANKNAME()))
                        .othersAccount(handleNull(data.getOTHERSACCOUNT()))
                        .channel(handleNull(data.getCHANNEL()))
                        .appId(handleNull(data.getAPPID()))
                        .preApproved(handleNull(data.getPREAPPROVED()))
                        .beneficiaryName(handleNull(data.getBENEFICIARYNAME()))
                        .otherMobileNo(handleNull(data.getOTHERMOBILENO()))
                        .remitterName(handleNull(data.getREMITTERNAME()))
                        .approvalNum(handleNull(data.getAPPROVALNUM()))
                        .mccCode(handleNull(data.getMCCCODE()))
                        .serverIp(handleNull(data.getSERVERIP()))
                        .cbsRespDate(handleNull(data.getCBSRESPDATE()))
                        .initMode(handleNull(data.getINITMODE()))
                        .purposeCode(handleNull(data.getPURPOSECODE()))
                        .creditAccountType(handleNull(data.getCREDITACCOUNTTYPE()))
                        .creditIfsc(handleNull(data.getCREDITIFSC()))
                        .debitAccountType(handleNull(data.getDEBITACCOUNTTYPE()))
                        .debitIfsc(handleNull(data.getDEBITIFSC()))
                        .orderId(handleNull(data.getORDERID()))
                        .payeeCode(handleNull(data.getPAYEECODE()))
                        .payerCode(handleNull(data.getPAYERCODE()))
                        .subType(handleNull(data.getSUBTYPE()))
                        .remittorMobileNumber(handleNull(data.getREMITTORMOBILENUMBER()))
                        .payeeImei(handleNull(data.getPAYEEIMEI()))
                        .payerImei(handleNull(data.getPAYERIMEI()))
                        .gmtDate(handleNull(data.getGMTDATE()))
                        .payeeAccountType(handleNull(data.getPAYEEACCOUNTTYPE()))
                        .payerAccountType(handleNull(data.getPAYERACCOUNTTYPE()))
                        .payerConsentName(handleNull(data.getPAYERCONSENTNAME()))
                        .payerConsentType(handleNull(data.getPAYERCONSENTTYPE()))
                        .payerConsentValue(handleNull(data.getPAYERCONSENTVALUE()))
                        .adjCode(handleNull(data.getADJCODE()))
                        .adjFlag(handleNull(data.getADJFLAG()))
                        .createDate(handleNull(data.getCREATEDATE()))
                        .creditAcctNarration103(handleNull(data.getCREDITACCTNARRATION103()))
                        .de46(handleNull(data.getDE46()))
                        .debitAcctNarration102(handleNull(data.getDEBITACCTNARRATION102()))
                        .gst(handleNull(data.getGST()))
                        .loanNumber(handleNull(data.getLOANNUMBER()))
                        .localTime(handleNull(data.getLOCALTIME()))
                        .narration125(handleNull(data.getNARRATION125()))
                        .p2pTxnCharges(handleNull(data.getP2PTXNCHARGES()))
                        .schemeCode(handleNull(data.getSCHEMECODE()))
                        .txnAmount(handleNull(data.getTXNAMOUNT()))
                        .batchId(handleNull(data.getBATCHID()))
                        .baseAmount(handleNull(data.getBASEAMOUNT()))
                        .baseCurr(handleNull(data.getBASECURR()))
                        .conCode(handleNull(data.getCONCODE()))
                        .fx(handleNull(data.getFX()))
                        .mkup(handleNull(data.getMKUP()))
                        .additional4(handleNull(data.getADDITIONAL4()))
                        .d12(handleNull(data.getD12()))
                        .additional1(handleNull(data.getADDITIONAL1()))
                        .additional2(handleNull(data.getADDITIONAL2()))
                        .additional3(handleNull(data.getADDITIONAL3()))
                        .additional5(handleNull(data.getADDITIONAL5()))
                        .cbsRouting(handleNull(data.getCBSROUTING()))
                        .build();
        }

        private static String handleNull(Object value) {
                return value != null ? value.toString() : null; // Convert null to null string
        }
}
