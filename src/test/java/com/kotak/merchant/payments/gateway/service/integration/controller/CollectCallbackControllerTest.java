package com.kotak.merchant.payments.gateway.service.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.EventManagementServiceApplication;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Service.CollectCallbackService;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils.EpochProvider;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.enums.CollectCallbackStatus;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.repository.dao.CollectCallbackDao;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.requests.ApiCreateCollectCallbackRequest;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.responses.ApiCreateCollectCallbackResponse;
import com.kotak.merchant.payments.gateway.service.integration.IntegrationTestBase;
import com.kotak.merchant.payments.gateway.service.integration.config.ConfigForTests;
import com.kotak.merchant.payments.gateway.service.integration.config.SetupConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static com.kotak.merchant.payments.gateway.service.Event_Management_Service.controller.CollectCallbackController.EP_CREATE_COLLECT_CALLBACK;
import static com.kotak.merchant.payments.gateway.service.integration.testutils.DataGen.apiCreateCollectCallbackRequestResponseCollectCallback;
import static com.kotak.merchant.payments.gateway.service.integration.testutils.DataGen.randApiCreateCollectCallback;
import static com.kotak.merchant.payments.gateway.service.integration.testutils.MockMvcUtils.postx;
import static com.kotak.merchant.payments.gateway.service.testutils.MapperUtils.getMapperWithTimeRegistered;
import static com.kotak.merchant.payments.gateway.service.testutils.RandUtils.randInstant;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = EventManagementServiceApplication.class)
@AutoConfigureMockMvc
@Import({ConfigForTests.class, SetupConfig.class})
@DirtiesContext
class CollectCallbackControllerTest extends IntegrationTestBase {

    private static final ObjectMapper mapper = getMapperWithTimeRegistered();

    @MockBean
    private EpochProvider epochProvider;

    @Autowired
    private CollectCallbackService svc;

    @Autowired
    private CollectCallbackDao collectCallbackDao;

    @Autowired
    public void setAccountSummaryDao(CollectCallbackDao collectCallbackDao) {
        this.collectCallbackDao = collectCallbackDao;
    }

    private MockMvc mvc;

    @Autowired
    public void setMvc(MockMvc mvc) {
        this.mvc = mvc;
    }

    private ApiCreateCollectCallbackRequest req;
    private Instant currInstant;

    @BeforeEach
    public void setup() {
        currInstant = randInstant();
        req = randApiCreateCollectCallback();
    }

    @Test
    void createTransaction_OnNewRequest_CreatesTransaction() throws Exception {
        var expected = success(req);
        mvc.perform(postx(req, EP_CREATE_COLLECT_CALLBACK ))
                .andExpect(status().isCreated());
//                .andExpect(bodyIs(expected, "creationTime"));
        // TODO: Extend test to read back from Kafka queue
    }

    private ApiCreateCollectCallbackResponse success(ApiCreateCollectCallbackRequest req) {
        var collectCallback = apiCreateCollectCallbackRequestResponseCollectCallback(req);
        return ApiCreateCollectCallbackResponse.builder()
                .collectCallbackStatus(CollectCallbackStatus.SUCCESS_CREATED_NOW)
                .collectCallback(collectCallback)
                .build();
    }
}
