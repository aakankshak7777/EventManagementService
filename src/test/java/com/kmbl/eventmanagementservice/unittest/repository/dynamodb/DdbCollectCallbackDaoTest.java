package com.kmbl.eventmanagementservice.unittest.repository.dynamodb;

import static com.kmbl.eventmanagementservice.UnitDataGen.randCredit;
import static com.kmbl.eventmanagementservice.UnitDataGen.randTransactionId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kmbl.eventmanagementservice.dao.CollectCallbackDao;
import com.kmbl.eventmanagementservice.dao.DdbCollectCallbackDao;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.exceptions.CollectCallbackExistsException;
import com.kmbl.eventmanagementservice.unittest.DynamoDbSetupBase;
import com.kmbl.eventmanagementservice.utils.EMSMetricUtil;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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
