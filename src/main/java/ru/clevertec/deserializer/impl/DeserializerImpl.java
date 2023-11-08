package ru.clevertec.deserializer.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.clevertec.deserializer.Deserializer;

public class DeserializerImpl implements Deserializer {
    @Override
    public <T> T deserialize(String content, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object target = clazz.getConstructor().newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            String fieldTypeSimpleNameLowerCase = fieldType.getSimpleName().toLowerCase();
            String fieldName = field.getName();
            if (fieldType.isPrimitive()) {
                String stringPattern = "\"" + fieldName + "\":" + "(true|false|\\d+)";
                Pattern pattern = Pattern.compile(stringPattern);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String coincidence = matcher.group();
                    String value = coincidence.split(":")[1];
                    switch (fieldType.getName().toLowerCase()) {
                        case "int" -> field.setInt(target, Integer.parseInt(value));
                        case "long" -> field.setLong(target, Long.parseLong(value));
                    }
                    content = content.replace(coincidence, "");
                }
            } else if (fieldTypeSimpleNameLowerCase.equals("string")) {
                String stringPattern = "\"" + fieldName + "\":" + "\".*?\"";
                Pattern pattern = Pattern.compile(stringPattern);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String coincidence = matcher.group();
                    String rawValue = coincidence.split(":")[1];
                    String value = rawValue.substring(1, rawValue.length() - 1);
                    field.set(target, value);
                    content = content.replace(coincidence, "");
                }
            } else if (fieldType.isEnum()) {
                String stringPattern = "\"" + fieldName + "\":" + "\".*?\"";
                Pattern pattern = Pattern.compile(stringPattern);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String coincidence = matcher.group();
                    String rawValue = coincidence.split(":")[1];
                    String value = rawValue.substring(1, rawValue.length() - 1);
                    Object enumVal = Enum.valueOf((Class<Enum>) fieldType, value);
                    field.set(target, enumVal);
                    content = content.replace(coincidence, "");
                }
            } else if (fieldType.isArray()) {
                String stringPattern = "\"" + fieldName + "\":\\[" + "\".*?\"\\]";
                Pattern pattern = Pattern.compile(stringPattern);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String coincidence = matcher.group();
                    String rawValue = coincidence.split(":")[1];
                    String[] stringArray = rawValue.substring(2, rawValue.length() - 2).split("\",\"");
                    field.set(target, stringArray);
                    content = content.replace(coincidence, "");
                }
            } else if (fieldTypeSimpleNameLowerCase.equalsIgnoreCase("LocalDate")) {
                String stringPattern = "\"" + fieldName + "\":" + "\".*?\"";
                Pattern pattern = Pattern.compile(stringPattern);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String coincidence = matcher.group();
                    String rawValue = coincidence.split(":")[1];
                    String value = rawValue.substring(1, rawValue.length() - 1);
                    LocalDate localDate = LocalDate.parse(value);
                    field.set(target, localDate);
                    content = content.replace(coincidence, "");
                }
            } else {
                Object another = deserialize(content, fieldType);
                field.set(target, another);

            }
        }
        return (T) target;
    }
}
