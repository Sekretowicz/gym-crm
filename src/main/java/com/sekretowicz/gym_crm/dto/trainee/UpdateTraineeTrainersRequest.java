package com.sekretowicz.gym_crm.dto.trainee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateTraineeTrainersRequest {
    @NotEmpty
    @Schema(description = "List of trainer usernames", required = true)
    private List<String> trainers;
}