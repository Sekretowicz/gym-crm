package com.sekretowicz.gym_crm.dto.workload;

import com.sekretowicz.gym_crm.model.Training;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class WorkloadRequestDto {
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private LocalDate trainingDate;
    private Integer trainingDuration;
    private String actionType;

    public WorkloadRequestDto (Training training, String actionType) {
        this.trainerUsername = training.getTrainer().getUser().getUsername();
        this.firstName = training.getTrainer().getUser().getFirstName();
        this.lastName = training.getTrainer().getUser().getLastName();
        this.isActive = training.getTrainer().getUser().getIsActive();
        this.trainingDate = training.getTrainingDate();
        this.trainingDuration = training.getTrainingDuration();
        this.actionType = actionType;
    }
}