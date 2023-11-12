package ru.clevertec.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonSerializer {

    private final ObjectMapper mapper;

    public JacksonSerializer() {
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }


    public String serialize(Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }

    public Object deserialize(Class<?> clazz, String json) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }
}
