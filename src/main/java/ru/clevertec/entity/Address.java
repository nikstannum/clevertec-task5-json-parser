package ru.clevertec.entity;

import lombok.Data;

@Data
public class Address {
    private City city;
    private String street;
    private int building;
}
