package com.sekretowicz.gym_crm.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CredentialsResponse {
    @Schema(description = "Generated username")
    private String username;

    @Schema(description = "Generated password")
    private String password;
}