package com.kmbl.eventmanagementservice.integration.testutils;

import static com.kmbl.eventmanagementservice.testUtils.MapperUtils.getMapperWithTimeRegistered;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MockMvcUtils {

    private static final ObjectMapper mapper = getMapperWithTimeRegistered();

    @SneakyThrows
    public static MockHttpServletRequestBuilder postx(Object body, String urlTemplate, Object... uriVariables) {
        return MockMvcRequestBuilders.post(urlTemplate, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body));
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder putx(Object body, String urlTemplate, Object... uriVariables) {
        return MockMvcRequestBuilders.put(urlTemplate, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body));
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder patchx(Object body, String urlTemplate, Object... uriVariables) {
        return MockMvcRequestBuilders.patch(urlTemplate, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body));
    }
}
