package com.sekretowicz.gym_crm.dto_legacy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraineeDto {
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<ShortTrainerDto> trainersList;

    public TraineeDto(Trainee trainee, boolean loadTrainers) {
        if (trainee.getUser() != null) {
            User user = trainee.getUser();
            this.username = user.getUsername();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.isActive = user.isActive();
        }
        this.dateOfBirth = trainee.getDateOfBirth();
        this.address = trainee.getAddress();

        if (loadTrainers && trainee.getTrainers() != null) {
            this.trainersList = trainee.getTrainers().stream()
                    .map(ShortTrainerDto::new)
                    .toList();
        }
    }
}
