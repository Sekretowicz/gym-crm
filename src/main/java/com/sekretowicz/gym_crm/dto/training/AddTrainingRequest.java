package com.sekretowicz.gym_crm.dto.training;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTrainingRequest {
    @NotBlank
    @Schema(description = "Trainee username", required = true)
    private String traineeUsername;

    @NotBlank
    @Schema(description = "Trainer username", required = true)
    private String trainerUsername;

    @NotBlank
    @Schema(description = "Training name", required = true)
    private String trainingName;

    @NotNull
    @Schema(description = "Training date", required = true)
    private LocalDate trainingDate;

    @Schema(description = "Duration in minutes", required = true)
    private Integer trainingDuration;
}