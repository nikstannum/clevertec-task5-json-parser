package ru.clevertec.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;

@Data
public class User {
    private long id;
    private Address address;
    private String[] phones;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateBirth;
}
