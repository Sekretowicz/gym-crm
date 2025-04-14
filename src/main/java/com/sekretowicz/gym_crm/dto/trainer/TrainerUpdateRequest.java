package com.sekretowicz.gym_crm.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainerUpdateRequest {
    @NotBlank
    @Schema(description = "Username", required = true)
    private String username;

    @NotBlank
    @Schema(description = "First name", required = true)
    private String firstName;

    @NotBlank
    @Schema(description = "Last name", required = true)
    private String lastName;

    @Schema(description = "Is active", required = true)
    private Boolean isActive;

    public Boolean isActive() {
        return isActive != null && isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }
}