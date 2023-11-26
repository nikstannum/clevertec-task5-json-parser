package ru.clevertec.serializer.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import ru.clevertec.serializer.Serializer;

public class SerializerImpl implements Serializer {
    @Override
    public String serialize(Object obj) throws IllegalAccessException {
        StringBuilder builder = new StringBuilder();
        if (obj instanceof List<?> list) {
            builder.append("[");
            for (int i = 0; i < list.size(); i++) {
                Object item = list.get(i);
                serialize(builder, item);
                if (i != list.size() -1) {
                    builder.append(",");
                }
            }
            return builder.append("]").toString();
        }
        return serialize(builder, obj).toString();
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
                    case "long", "int", "boolean" -> appendPrimitive(builder, field, obj);
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
                .append(field.get(obj));
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
        String name = field.getType().getName();
        if (name.equals("long")) {
            builder.append(field.getLong(obj));

        } else if (name.equals("int")){
            builder.append(field.getInt(obj));
        } else {
            builder.append(field.getBoolean(obj));
        }
    }
}
