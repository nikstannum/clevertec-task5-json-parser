package ru.clevertec;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import ru.clevertec.deserializer.Deserializer;
import ru.clevertec.deserializer.impl.DeserializerImpl;
import ru.clevertec.entity.Address;
import ru.clevertec.entity.City;
import ru.clevertec.entity.User;
import ru.clevertec.jackson.JacksonSerializer;
import ru.clevertec.serializer.Serializer;
import ru.clevertec.serializer.impl.SerializerImpl;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Address address = new Address();
        address.setCity(City.MINSK);
        address.setStreet("Lenina");
        address.setBuilding(1);
        User user = new User();
        user.setId(123L);
        user.setAddress(address);
        String[] phones = new String[]{"+375291234567", "+375179876543"};
        user.setPhones(phones);
        user.setDateBirth(LocalDate.now());
        Serializer serializer = new SerializerImpl();
        String myJson = serializer.serialize(user);
        System.out.println(myJson);


        JacksonSerializer jackson = new JacksonSerializer();
        String jacksonString = jackson.serialize(user);
        System.out.println(jacksonString.equals(myJson));

        Deserializer deserializer = new DeserializerImpl();
        User u = deserializer.deserialize(myJson, User.class);
        System.out.println(u);
        System.out.println(u.equals(user));
    }
}
