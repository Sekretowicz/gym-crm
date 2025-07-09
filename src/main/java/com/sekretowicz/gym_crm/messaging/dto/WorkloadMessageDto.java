package com.sekretowicz.gym_crm.messaging.dto;

import com.sekretowicz.gym_crm.dto.training.VerboseTrainingResponse;
import com.sekretowicz.gym_crm.model.Training;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkloadMessageDto {
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private LocalDate trainingDate;
    private Integer trainingDuration;
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

    public WorkloadMessageDto(VerboseTrainingResponse trainingResponse) {
        this.trainerUsername = trainingResponse.getTrainer().getUsername();
        this.firstName = trainingResponse.getTrainer().getFirstName();
        this.lastName = trainingResponse.getTrainer().getLastName();
        this.isActive = trainingResponse.getTrainer().getIsActive();
        this.trainingDate = trainingResponse.getTrainingDate();
        this.trainingDuration = trainingResponse.getTrainingDuration();
        this.actionType = trainingResponse.getActionType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkloadMessageDto)) return false;

        WorkloadMessageDto that = (WorkloadMessageDto) o;

        if (!trainerUsername.equals(that.trainerUsername)) return false;
        if (!firstName.equals(that.firstName)) return false;
        if (!lastName.equals(that.lastName)) return false;
        if (!isActive.equals(that.isActive)) return false;
        if (!trainingDate.equals(that.trainingDate)) return false;
        if (!trainingDuration.equals(that.trainingDuration)) return false;
        if (!actionType.equals(that.actionType)) return false;
        return true;
    }
}