package ru.clevertec.serializer.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import ru.clevertec.serializer.Serializer;

public class SerializerImpl implements Serializer {
    @Override
    public String serialize(Object obj) throws IllegalAccessException {
        return serialize(new StringBuilder(), obj).toString();
    }

    private StringBuilder serialize(StringBuilder builder, Object obj) throws IllegalAccessException {
        builder.append("{");
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            String simpleName = fieldType.getSimpleName();
            if (simpleName.equals("String") || fieldType.isPrimitive()) {
                String simpleNameLowerCase = simpleName.toLowerCase();
                switch (simpleNameLowerCase) {
                    case "long", "int" -> appendPrimitive(builder, field, obj);
                    case "string" -> appendStringEnum(builder, field, obj);
                }
            } else if (simpleName.equals("Long")) {
                appendLong(builder, field, obj);
            } else if (fieldType.isEnum()) {
                appendStringEnum(builder, field, obj);
            } else if (fieldType.isArray()) {
                appendArray(builder, field, obj);
            } else if (simpleName.equalsIgnoreCase("LocalDate")) {
                appendLocalDate(builder, field, obj);
            } else {
                builder
                        .append("\"")
                        .append(field.getName())
                        .append("\":");
                serialize(builder, field.get(obj));
            }
            if (i != fieldsLength - 1) {
                builder.append(",");
            }

        }
        builder.append("}");
        return builder;
    }

    private void appendLong(StringBuilder builder, Field field, Object obj) throws IllegalAccessException {
        builder.append("\"")
                .append(field.getName())
                .append("\"")
                .append(":")
                .append(field.get(obj))
                .append("");
    }

    private void appendLocalDate(StringBuilder builder, Field field, Object obj) throws IllegalAccessException {
        builder
                .append("\"")
                .append(field.getName())
                .append("\":\"")
                .append(field.get(obj))
                .append("\"");
    }

    private void appendArray(StringBuilder builder, Field field, Object obj) throws IllegalAccessException {
        builder
                .append("\"")
                .append(field.getName())
                .append("\"")
                .append(":");
        Object array = field.get(obj);
        builder.append("[");
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            builder.append("\"")
                    .append(Array.get(array, i))
                    .append("\"");

            if (i != length - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
    }

    private void appendStringEnum(StringBuilder builder, Field field, Object obj) throws IllegalAccessException {
        builder.append("\"")
                .append(field.getName())
                .append("\"")
                .append(":\"")
                .append(field.get(obj))
                .append("\"");
    }

    private void appendPrimitive(StringBuilder builder, Field field, Object obj) throws IllegalAccessException {
        builder
                .append("\"")
                .append(field.getName())
                .append("\"")
                .append(":");
        if (field.getType().getName().equals("long")) {
            builder.append(field.getLong(obj));

        } else {
            builder.append(field.getInt(obj));
        }
    }
}
