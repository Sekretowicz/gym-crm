package com.sekretowicz.gym_crm.dto.training;


import com.sekretowicz.gym_crm.model.Training;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainingResponse {
    @Schema(description = "Training name")
    private String trainingName;

    @Schema(description = "Training date")
    private LocalDate trainingDate;

    @Schema(description = "Training type")
    private String trainingType;

    @Schema(description = "Duration in minutes")
    private int trainingDuration;

    @Schema(description = "Partner name (trainer or trainee)")
    private String partnerName;

    public TrainingResponse(Training training) {
        this.trainingName = training.getTrainingName();
        this.trainingDate = training.getTrainingDate();
        this.trainingType = training.getTrainingType().getTrainingTypeName();
        this.partnerName = training.getTrainer().getUser().getUsername();
    }
}