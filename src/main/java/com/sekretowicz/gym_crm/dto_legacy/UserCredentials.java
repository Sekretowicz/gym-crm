package com.sekretowicz.gym_crm.dto_legacy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {
    String username;
    String password;
    String token;
}
