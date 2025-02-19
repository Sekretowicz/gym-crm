package com.sekretowicz.gym_crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {
    private long id;
    private LocalDate birthDate;
    private String address;
    private long userId;
}
