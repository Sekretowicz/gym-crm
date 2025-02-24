package com.sekretowicz.gym_crm.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Training {
    private long id;
    private long traineeId;
    private long trainerId;
    private String name;
    private long trainingTypeId;
    private LocalDate date;
    private int duration;
}
