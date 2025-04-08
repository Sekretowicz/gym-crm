package com.sekretowicz.gym_crm.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank
    @Schema(description = "Username", required = true)
    private String username;

    @NotBlank
    @Schema(description = "Old password", required = true)
    private String oldPassword;

    @NotBlank
    @Schema(description = "New password", required = true)
    private String newPassword;
}