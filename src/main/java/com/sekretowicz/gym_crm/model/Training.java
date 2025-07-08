package com.sekretowicz.gym_crm.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
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

    //04.07 Override equals for comparing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Training)) return false;
        Training training = (Training) o;

        if (id != training.getId()) {
            return false;
        }
        if (trainingName != training.getTrainingName()) {
            return false;
        }
        if (trainingDate != training.getTrainingDate()) {
            return false;
        }
        if (trainingDuration != training.getTrainingDuration()) {
            return false;
        }
        //Sanity check for trainee and trainer. It can't be null, but we check it just in case
        if (trainee == null || trainer == null || training.getTrainee() == null || training.getTrainer() == null) {
            return false;
        }
        if (!trainee.equals(training.trainee) || !trainer.equals(training.trainer)) {
            return false;
        }
        return true;
    }
}
