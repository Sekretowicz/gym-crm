package com.sekretowicz.gym_crm.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetActiveRequest {
    @NotBlank
    @Schema(description = "Username", required = true)
    private String username;

    @Schema(description = "Active flag", required = true)
    private Boolean isActive;

    public Boolean isActive() {
        return isActive != null && isActive;
    }

    public void setActive (Boolean active) {
        this.isActive = active;
    }
}