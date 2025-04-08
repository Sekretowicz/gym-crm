package com.sekretowicz.gym_crm.dto_legacy;

import com.sekretowicz.gym_crm.model.Training;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TrainingDto {
    private String trainingName;
    private LocalDate trainingDate;
    private String trainingType;
    private int trainingDuration;
    private String trainerName;
    private String traineeName;

    public TrainingDto(Training training) {
        this.trainingName = training.getTrainingName();
        this.trainingDate = training.getTrainingDate();
        this.trainingType = training.getTrainingType().getTrainingTypeName();
        this.trainingDuration = training.getTrainingDuration();
        this.trainerName = training.getTrainer().getUser().getUsername();
        this.traineeName = training.getTrainee().getUser().getUsername();
    }
}
