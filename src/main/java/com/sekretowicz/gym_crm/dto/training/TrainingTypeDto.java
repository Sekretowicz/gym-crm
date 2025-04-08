package com.sekretowicz.gym_crm.dto.training;


import com.sekretowicz.gym_crm.model.TrainingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TrainingTypeDto {
    @Schema(description = "Type ID")
    private Long id;

    @Schema(description = "Training type name")
    private String type;

    public TrainingTypeDto(TrainingType type) {
        this.id = type.getId();
        //this.type = type.getType();
    }
}