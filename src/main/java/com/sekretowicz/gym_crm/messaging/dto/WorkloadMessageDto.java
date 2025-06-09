package com.sekretowicz.gym_crm.messaging.dto;

import com.sekretowicz.gym_crm.model.Training;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class WorkloadMessageDto {
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private int trainingDuration;
    private String actionType;

    public WorkloadMessageDto(Training training, String actionType) {
        this.trainerUsername = training.getTrainer().getUser().getUsername();
        this.firstName = training.getTrainer().getUser().getFirstName();
        this.lastName = training.getTrainer().getUser().getLastName();
        this.isActive = training.getTrainer().getUser().isActive();
        this.trainingDate = training.getTrainingDate();
        this.trainingDuration = training.getTrainingDuration();
        this.actionType = actionType;
    }
}