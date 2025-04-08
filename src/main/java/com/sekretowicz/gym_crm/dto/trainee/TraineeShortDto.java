package com.sekretowicz.gym_crm.dto.trainee;


import com.sekretowicz.gym_crm.model.Trainee;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TraineeShortDto {
    @Schema(description = "Username")
    private String username;

    @Schema(description = "First name")
    private String firstName;

    @Schema(description = "Last name")
    private String lastName;

    public TraineeShortDto(Trainee trainee) {
        this.username = trainee.getUser().getUsername();
        this.firstName = trainee.getUser().getFirstName();
        this.lastName = trainee.getUser().getLastName();
    }
}