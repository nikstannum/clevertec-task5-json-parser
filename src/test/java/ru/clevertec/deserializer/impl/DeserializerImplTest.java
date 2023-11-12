package ru.clevertec.deserializer.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.entity.User;
import ru.clevertec.jackson.JacksonSerializer;
import ru.clevertec.util.TestData;

class DeserializerImplTest {
    private DeserializerImpl deserializer;
    private JacksonSerializer jackson;

    @BeforeEach
    void setUp() {
        deserializer = new DeserializerImpl();
        jackson = new JacksonSerializer();
    }

    @Test
    void deserialize() throws Exception {
        // given
        User expected = TestData.builder().build().buildUser();
        String json = jackson.serialize(expected);

        // when
        User actual = deserializer.deserialize(json, User.class);

        // then
        Assertions.assertEquals(expected, actual);
    }
}