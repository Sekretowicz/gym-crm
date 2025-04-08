package com.sekretowicz.gym_crm.dto_legacy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainerDto {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private List<ShortTraineeDto> traineesList;

    public TrainerDto(Trainer trainer, boolean loadTrainees) {
        if (trainer.getUser() != null) {
            User user = trainer.getUser();
            this.username = user.getUsername();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.isActive = user.isActive();
        }
        this.specialization = trainer.getSpecialization();

        if (loadTrainees && trainer.getTrainees() != null ) {
            this.traineesList = trainer.getTrainees().stream()
                    .map(ShortTraineeDto::new)
                    .toList();
        }
    }
}
