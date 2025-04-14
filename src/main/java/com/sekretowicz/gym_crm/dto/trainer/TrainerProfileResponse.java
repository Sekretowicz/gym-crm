package com.sekretowicz.gym_crm.dto.trainer;


import com.sekretowicz.gym_crm.dto.trainee.TraineeShortDto;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.TrainingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TrainerProfileResponse {
    @Schema(description = "Username")
    private String username;

    @Schema(description = "First name")
    private String firstName;

    @Schema(description = "Last name")
    private String lastName;

    @Schema(description = "Specialization")
    private TrainingType specialization;

    @Schema(description = "Is active")
    private boolean isActive;

    @Schema(description = "List of trainees")
    private List<TraineeShortDto> traineesList;

    public TrainerProfileResponse(Trainer trainer) {
        this.username = trainer.getUser().getUsername();
        this.firstName = trainer.getUser().getFirstName();
        this.lastName = trainer.getUser().getLastName();
        this.specialization = trainer.getSpecialization();
        this.isActive = trainer.getUser().isActive();
    }
}