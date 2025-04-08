package com.sekretowicz.gym_crm.dto.trainer;


import com.sekretowicz.gym_crm.model.Trainer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TrainerShortDto {
    @Schema(description = "Username")
    private String username;

    @Schema(description = "First name")
    private String firstName;

    @Schema(description = "Last name")
    private String lastName;

    @Schema(description = "Specialization")
    private String specialization;

    public TrainerShortDto(Trainer trainer) {
        this.username = trainer.getUser().getUsername();
        this.firstName = trainer.getUser().getFirstName();
        this.lastName = trainer.getUser().getLastName();
        this.specialization = trainer.getSpecialization();
    }
}