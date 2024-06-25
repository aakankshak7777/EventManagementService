package com.kmbl.eventmanagementservice.unittest.repository.dynamodb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kmbl.eventmanagementservice.dao.CollectCallbackDao;
import com.kmbl.eventmanagementservice.dao.DdbCollectCallbackDao;
import com.kmbl.eventmanagementservice.enums.EventName;
import com.kmbl.eventmanagementservice.exceptions.CollectCallbackExistsException;
import com.kmbl.eventmanagementservice.testUtils.UnitDataGenUtils;
import com.kmbl.eventmanagementservice.unittest.DynamoDbSetupBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DdbCollectCallbackDaoTest extends DynamoDbSetupBase {
    private CollectCallbackDao dao;


    @BeforeEach
    public void setUp() {
        dao = new DdbCollectCallbackDao(ddb);
    }

    @Test
    void create_OnNewRequest_CreatesANewCollectCallback() {
        var collectCallback = UnitDataGenUtils.randCredit(EventName.COLLECT_CALLBACK_API);
        dao.create(collectCallback);
        var created = dao.getByTransactionIdAndType(collectCallback.transactionId(),collectCallback.type());
        assertThat(created).hasValue(collectCallback);
    }

    @Test
    void create_OnDuplicate_Fails() {
        var collectCallback = UnitDataGenUtils.randCredit(EventName.COLLECT_CALLBACK_API);
        dao.create(collectCallback);

        assertThatThrownBy(() -> dao.create(collectCallback)).isInstanceOf(CollectCallbackExistsException.class);
    }

    @Test
    void get_UnknownTransaction_ReturnsEmpty() {
        var actual = dao.getByTransactionIdAndType(UnitDataGenUtils.randTransactionId(),"type");
        assertThat(actual).isEmpty();
    }
}
