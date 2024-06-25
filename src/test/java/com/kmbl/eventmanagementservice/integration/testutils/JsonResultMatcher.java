package com.kmbl.eventmanagementservice.integration.testutils;

import static com.kmbl.eventmanagementservice.testutils.MapperUtils.getMapperWithTimeRegistered;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

public class JsonResultMatcher implements ResultMatcher {

    private static final ObjectMapper mapper = getMapperWithTimeRegistered();

    private final Object expected;
    private final String[] fieldsToExclude;

    public JsonResultMatcher(Object expected, String... fieldsToExclude) {
        this.expected = expected;
        this.fieldsToExclude = fieldsToExclude;
    }

    @Override
    public void match(MvcResult result) throws Exception {
        Object actual = mapper.readValue(result.getResponse().getContentAsString(), expected.getClass());
        if (fieldsToExclude.length == 0) {
            assertThat(actual).isEqualTo(expected);
        } else {
            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringFields(fieldsToExclude)
                    .isEqualTo(expected);
        }
    }

    public static JsonResultMatcher bodyIs(Object expected, String... fieldsToExclude) {
        return new JsonResultMatcher(expected, fieldsToExclude);
    }
}
