package ru.clevertec.serializer;

public interface Serializer {
    String serialize(Object obj) throws IllegalAccessException;
}
