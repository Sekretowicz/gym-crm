package com.sekretowicz.gym_crm.dto.trainee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TraineeUpdateRequest {
    @NotBlank
    @Schema(description = "Username", required = true, example = "ivan.petrov")
    private String username;

    @NotBlank
    @Schema(description = "First name", required = true)
    private String firstName;

    @NotBlank
    @Schema(description = "Last name", required = true)
    private String lastName;

    @Schema(description = "Date of birth")
    private LocalDate dateOfBirth;

    @Schema(description = "Address")
    private String address;

    @Schema(description = "Is active", required = true)
    private boolean isActive;
}