package ru.clevertec.util;

import java.time.LocalDate;
import lombok.Builder;
import ru.clevertec.entity.Address;
import ru.clevertec.entity.City;
import ru.clevertec.entity.User;

@Builder
public class TestData {
    public User buildUser() {
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
        return user;
    }
}
