package com.sekretowicz.gym_crm.dto_legacy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.TrainingType;
import com.sekretowicz.gym_crm.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShortTrainerDto {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private Boolean isActive;

    public ShortTrainerDto(Trainer trainer) {
        if (trainer.getUser() != null) {
            User user = trainer.getUser();
            this.username = user.getUsername();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.isActive = user.isActive();
        }
        this.specialization = trainer.getSpecialization();
    }
}
