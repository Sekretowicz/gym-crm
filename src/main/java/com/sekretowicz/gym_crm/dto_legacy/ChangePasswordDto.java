package com.sekretowicz.gym_crm.dto_legacy;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String username;
    private String oldPassword;
    private String newPassword;
}
