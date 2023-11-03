package ru.clevertec.entity;

public class Address {
    private City city;
    private String street;
    private int building;

    enum City {
        MINSK, HRODNA, VITEBSK, GOMEL
    }
}
