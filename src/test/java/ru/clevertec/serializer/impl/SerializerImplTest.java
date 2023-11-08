package ru.clevertec.serializer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.entity.User;
import ru.clevertec.jackson.JacksonSerializer;
import ru.clevertec.util.TestData;

class SerializerImplTest {
    private SerializerImpl serializer;
    private JacksonSerializer jackson;

    @BeforeEach
    void setUp() {
        serializer = new SerializerImpl();
        jackson = new JacksonSerializer();
    }

    @Test
    void serialize() throws IllegalAccessException, JsonProcessingException {
        // given
        User user = TestData.builder().build().buildUser();

        String expected = jackson.serialize(user);

        // when
        String actual = serializer.serialize(user);

        // then
        Assertions.assertEquals(expected, actual);

    }
}