package ru.clevertec.deserializer;

import java.lang.reflect.InvocationTargetException;

public interface Deserializer {
    <T> T deserialize(String content, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
