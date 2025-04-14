package com.sekretowicz.gym_crm.dto.trainee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistrationRequest {
    @NotBlank
    @Schema(description = "First name", required = true, example = "Ivan")
    private String firstName;

    @NotBlank
    @Schema(description = "Last name", required = true, example = "Petrov")
    private String lastName;

    @Schema(description = "Date of birth", example = "2000-01-01")
    private LocalDate dateOfBirth;

    @Schema(description = "Address", example = "Lenina st., 10")
    private String address;
}