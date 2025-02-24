package com.sekretowicz.gym_crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Trainer {
    private long id;
    private long userId;
    private long trainingTypeId;
}