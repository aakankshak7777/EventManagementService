package com.kmbl.eventmanagementservice.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MapperUtils {
    public static ObjectMapper getMapperWithTimeRegistered() {
        ObjectMapper mapper;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
