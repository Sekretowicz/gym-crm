package com.sekretowicz.gym_crm.model;

import com.sekretowicz.gym_crm.utils.PasswordGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

    //TODO: Should it be here? I'm not sure yet, fix that later.
    @Autowired
    private PasswordGenerator passwordGenerator;

    public User(long id, String firstName, String lastName, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.username = firstName + "." + lastName;
        this.password = "passwordGenerator.generatePassword()";
    }
}
