//package com.kmbl.eventmanagementservice.integration.controller;
//
//import static com.kmbl.eventmanagementservice.controller.CollectCallbackController.EP_CREATE_COLLECT_CALLBACK;
//import static com.kmbl.eventmanagementservice.testUtils.MapperUtils.getMapperWithTimeRegistered;
//import static com.kmbl.eventmanagementservice.testUtils.RandUtils.randInstant;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.kmbl.eventmanagementservice.Config.ConfigForTests;
//import com.kmbl.eventmanagementservice.Config.SetupConfig;
//import com.kmbl.eventmanagementservice.EventManagementServiceApplication;
//import com.kmbl.eventmanagementservice.enums.CollectCallbackStatus;
//import com.kmbl.eventmanagementservice.integration.IntegrationTestBase;
//import com.kmbl.eventmanagementservice.integration.testutils.DataGen;
//import com.kmbl.eventmanagementservice.integration.testutils.MockMvcUtils;
//import com.kmbl.eventmanagementservice.requests.ApiCreateCollectCallbackRequest;
//import com.kmbl.eventmanagementservice.responses.ApiCreateCollectCallbackResponse;
//import com.kmbl.eventmanagementservice.utils.EpochProvider;
//import java.time.Instant;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//@ActiveProfiles("test")
//@Testcontainers
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = EventManagementServiceApplication.class)
//@AutoConfigureMockMvc
//@Import({ConfigForTests.class, SetupConfig.class})
//@DirtiesContext
//class CollectCallbackControllerTest extends IntegrationTestBase {
//
//    private static final ObjectMapper mapper = getMapperWithTimeRegistered();
//
//    @MockBean
//    private EpochProvider epochProvider;
//
//    private MockMvc mvc;
//
//    @Autowired
//    public void setMvc(MockMvc mvc) {
//        this.mvc = mvc;
//    }
//
//    private ApiCreateCollectCallbackRequest req;
//    private Instant currInstant;
//
//    @BeforeEach
//    public void setup() {
//        currInstant = randInstant();
//        req = DataGen.randApiCreateCollectCallback();
//    }
//
//    @Test
//    void createTransaction_OnNewRequest_CreatesTransaction() throws Exception {
//        var expected = success(req);
//        mvc.perform(MockMvcUtils.postx(req, EP_CREATE_COLLECT_CALLBACK ))
//                .andExpect(status().isCreated());
////                .andExpect(bodyIs(expected, "creationTime"));
//        // TODO: Extend test to read back from Kafka queue
//    }
//
//    private ApiCreateCollectCallbackResponse success(ApiCreateCollectCallbackRequest req) {
//        var collectCallback = DataGen.apiCreateCollectCallbackRequestResponseCollectCallback(req);
//        return ApiCreateCollectCallbackResponse.builder()
//                .collectCallbackStatus(CollectCallbackStatus.SUCCESS_CREATED_NOW)
//                .collectCallback(collectCallback)
//                .build();
//    }
//}
