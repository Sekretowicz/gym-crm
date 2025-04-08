package com.sekretowicz.gym_crm.dto.trainee;


import com.sekretowicz.gym_crm.dto.trainer.TrainerShortDto;
import com.sekretowicz.gym_crm.model.Trainee;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class TraineeProfileResponse {
    @Schema(description = "Username")
    private String username;

    @Schema(description = "First name")
    private String firstName;

    @Schema(description = "Last name")
    private String lastName;

    @Schema(description = "Date of birth")
    private LocalDate dateOfBirth;

    @Schema(description = "Address")
    private String address;

    @Schema(description = "Is active")
    private boolean isActive;

    @Schema(description = "List of assigned trainers")
    private List<TrainerShortDto> trainersList;

    public TraineeProfileResponse(Trainee trainee) {
        this.username = trainee.getUser().getUsername();
        this.firstName = trainee.getUser().getFirstName();
        this.lastName = trainee.getUser().getLastName();
        this.dateOfBirth = trainee.getDateOfBirth();
        this.address = trainee.getAddress();
        this.isActive = trainee.getUser().isActive();
    }
}