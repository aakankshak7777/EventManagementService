package com.kotak.merchant.payments.gateway.service.unittest.repository.dynamodb;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EMSMetricUtil;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.EventName;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.exceptions.CollectCallbackExistsException;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.repository.dao.CollectCallbackDao;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.repository.dao.DdbCollectCallbackDao;
import com.kotak.merchant.payments.gateway.service.unittest.DynamoDbSetupBase;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import static com.kotak.merchant.payments.gateway.service.unittest.UnitDataGen.randCredit;
import static com.kotak.merchant.payments.gateway.service.unittest.UnitDataGen.randTransactionId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class DdbCollectCallbackDaoTest extends DynamoDbSetupBase {
    private CollectCallbackDao dao;
    private EMSMetricUtil metricUtil;

    @BeforeEach
    public void setUp() {
        metricUtil = new EMSMetricUtil(new SimpleMeterRegistry());
        dao = new DdbCollectCallbackDao(ddb, metricUtil);
    }

    @Test
    void create_OnNewRequest_CreatesANewCollectCallback() {
        var collectCallback = randCredit(EventName.COLLECT_CALLBACK_API);
        dao.create(collectCallback);
        var created = dao.getByTransactionIdAndType(collectCallback.transactionId(),collectCallback.type());
        assertThat(created).hasValue(collectCallback);
    }

    @Test
    void create_OnDuplicate_Fails() {
        var collectCallback = randCredit(EventName.COLLECT_CALLBACK_API);
        dao.create(collectCallback);

        assertThatThrownBy(() -> dao.create(collectCallback)).isInstanceOf(CollectCallbackExistsException.class);
    }

    @Test
    void get_UnknownTransaction_ReturnsEmpty() {
        var actual = dao.getByTransactionIdAndType(randTransactionId(),"type");
        assertThat(actual).isEmpty();
    }
}
