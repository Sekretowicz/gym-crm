package com.sekretowicz.gym_crm.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerRegistrationRequest {
    @NotBlank
    @Schema(description = "First name", required = true)
    private String firstName;

    @NotBlank
    @Schema(description = "Last name", required = true)
    private String lastName;

    @NotBlank
    @Schema(description = "Specialization", required = true)
    private Long specId;        //ID of specialization
}