package com.sekretowicz.gym_crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Trainee {
    private long id;
    private LocalDate birthDate;
    private String address;
    private long userId;
}
