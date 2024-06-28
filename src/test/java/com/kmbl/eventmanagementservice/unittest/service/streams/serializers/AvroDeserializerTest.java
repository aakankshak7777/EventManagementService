package com.kmbl.eventmanagementservice.unittest.service.streams.serializers;

import static org.assertj.core.api.Assertions.assertThat;

import com.kmbl.eventmanagementservice.Schema.CBSTransactionLogs;
import com.kmbl.eventmanagementservice.service.streams.serializers.AvroDeserializer;
import com.kmbl.eventmanagementservice.service.streams.serializers.CBSTransactionLogsDeserializer;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AvroDeserializerTest {
    private AvroDeserializer<CBSTransactionLogs> cbsTransactionLogsAvroDeserializer;


    @BeforeEach
    public void setUp() {
        cbsTransactionLogsAvroDeserializer = new CBSTransactionLogsDeserializer();
    }

    @Test
    @SneakyThrows
    public void deserialize_olderWriterSchema_returnsDeserializedEvent() {
        var event = "{\"table\": null, \"op_type\": \"I\", \"op_ts\": \"1988-09-24 13:22:23.951000\", \"current_ts\": \"2022-06-08 06:50:10.451000\", \"pos\": \"1\", \"source_scn\": \"1\", \"before\": null, \"after\": {\"ID\": null, \"AMOUNT\": \"200\", \"CBSRC\": \"00\", \"CREDITACCOUNT\": \"1234\", \"CUSTOMERID\": null, \"DEBITACCOUNT\": \"1234\", \"MSGID\": null, \"REVERSAL\": null, \"REVERSALSTATUS\": null, \"RRN\": null, \"STAN\": null, \"TXN_DATE\": null, \"TXNID\": \"E8BMLBO67Nc3CkMX\", \"TYPE\": null, \"UPIRC\": null, \"ORGTXNID\": null, \"REMARKS\": null, \"CUSTOMERACCOUNT\": null, \"NAME\": null, \"PAYEEVPA\": null, \"PAYERVPA\": null, \"SEQNO\": null, \"COMMISSIONAMOUNT\": null, \"CUSTOMERREFID\": null, \"TRANTYPE\": \"debit\", \"CREDITBANKNAME\": null, \"DEBITBANKNAME\": null, \"OTHERSACCOUNT\": null, \"CHANNEL\": null, \"APPID\": null, \"PREAPPROVED\": null, \"BENEFICIARYNAME\": null, \"OTHERMOBILENO\": null, \"REMITTERNAME\": null, \"APPROVALNUM\": null, \"MCCCODE\": null, \"SERVERIP\": null, \"CBSRESP_DATE\": null, \"INITMODE\": null, \"PURPOSECODE\": null, \"CREDITACCOUNTTYPE\": null, \"CREDITIFSC\": null, \"DEBITACCOUNTTYPE\": null, \"DEBITIFSC\": null, \"ORDERID\": null, \"PAYEECODE\": null, \"PAYERCODE\": null, \"SUBTYPE\": null, \"REMITTORMOBILENUMBER\": null, \"PAYEEIMEI\": null, \"PAYERIMEI\": null, \"GMTDATE\": null, \"PAYEEACCOUNTTYPE\": null, \"PAYERACCOUNTTYPE\": null, \"PAYERCONSENTNAME\": null, \"PAYERCONSENTTYPE\": null, \"PAYERCONSENTVALUE\": null, \"ADJCODE\": null, \"ADJFLAG\": null, \"CREATE_DATE\": null, \"CREDITACCTNARRATION_103\": null, \"DE46\": null, \"DEBITACCTNARRATION_102\": null, \"GST\": null, \"LOANNUMBER\": null, \"LOCALTIME\": null, \"NARRATION_125\": null, \"P2PTXNCHARGES\": null, \"SCHEMECODE\": null, \"TXNAMOUNT\": null, \"BATCH_ID\": null, \"BASEAMOUNT\": null, \"BASECURR\": null, \"CONCODE\": null, \"FX\": null, \"MKUP\": null, \"ADDITIONAL4\": null, \"D12\": null, \"ADDITIONAL1\": null, \"ADDITIONAL2\": null, \"ADDITIONAL3\": null, \"ADDITIONAL5\": null, \"CBS_ROUTING\": null}}";
        String hexEvent =
                "000202490234313938382d30392d32342031333a32323a32332e3935313030300234323032322d30362d30382030363a35303a31302e3435313030300202310202310002000206323030020430300208313233340002083132333400000000000002204538424d4c424f36374e6333436b4d580000000000000000000000020a646562697400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        CBSTransactionLogs deserializeEvent = cbsTransactionLogsAvroDeserializer.deserialize("test-123", Hex.decodeHex(hexEvent));
        assertThat(deserializeEvent.toString())
                .isEqualTo(event);
    }


}
