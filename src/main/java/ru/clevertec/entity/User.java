package ru.clevertec.entity;

import lombok.Data;

@Data
public class User {
    private long id;
    private Address address;
    private String[] phones;
}
