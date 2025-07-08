package com.sekretowicz.gym_crm.dto.training;

import com.sekretowicz.gym_crm.dto_legacy.ShortTraineeDto;
import com.sekretowicz.gym_crm.dto_legacy.ShortTrainerDto;
import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.Training;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class VerboseTrainingResponse {
    private ShortTraineeDto traineeDetails;
    private ShortTrainerDto trainerDetails;
    private Boolean isActive;
    private LocalDate trainingDate;
    private Integer trainingDuration;
    private String actionType;

    public VerboseTrainingResponse(Training training) {
        this.traineeDetails = new ShortTraineeDto(training.getTrainee());
        this.trainerDetails = new ShortTrainerDto(training.getTrainer());
        this.isActive = training.getTrainer().getUser().isActive();
        this.trainingDate = training.getTrainingDate();
        this.trainingDuration = training.getTrainingDuration();
        this.actionType = "ADD"; // Default action type, can be changed based on context
    }
}
