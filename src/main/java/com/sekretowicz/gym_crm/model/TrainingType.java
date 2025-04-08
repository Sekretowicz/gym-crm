package com.sekretowicz.gym_crm.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String trainingTypeName;

    public TrainingType(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}
