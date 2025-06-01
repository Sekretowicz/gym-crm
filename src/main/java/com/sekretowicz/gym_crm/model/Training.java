package com.sekretowicz.gym_crm.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String trainingName;
    private LocalDate trainingDate;
    private int trainingDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id", nullable = false)
    private Trainee trainee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType trainingType;
}
