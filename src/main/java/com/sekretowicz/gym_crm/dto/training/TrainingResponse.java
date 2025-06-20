package com.sekretowicz.gym_crm.dto.training;

import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.model.TrainingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainingResponse {
    //Id is important too
    @Schema(description = "Training ID")
    private Long id;

    @Schema(description = "Training name")
    private String trainingName;

    @Schema(description = "Training date")
    private LocalDate trainingDate;

    @Schema(description = "Training type")
    private TrainingType trainingType;

    @Schema(description = "Duration in minutes")
    private int trainingDuration;

    //29.05.2025    Now we'll store both trainer and trainee names in the training response
    @Schema(description = "Trainer name")
    private String trainerName;

    @Schema(description = "Trainee name")
    private String traineeName;

    public TrainingResponse(Training training) {
        this.id = training.getId();
        this.trainingName = training.getTrainingName();
        this.trainingDate = training.getTrainingDate();
        this.trainingType = training.getTrainingType();
        this.trainingDuration = training.getTrainingDuration();
        this.trainerName = training.getTrainer().getUser().getUsername();
        this.traineeName = training.getTrainee().getUser().getUsername();
    }
}