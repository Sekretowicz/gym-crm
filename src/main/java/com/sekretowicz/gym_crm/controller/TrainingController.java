package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.dto.training.AddTrainingRequest;
import com.sekretowicz.gym_crm.dto.training.TrainingResponse;
import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainings")
@Tag(name = "Training Controller", description = "Training management")
public class TrainingController {
    @Autowired
    private TrainingService service;

    //14. Add Training (POST method)
    @PostMapping
    @Operation(summary = "Add new training", description = "Create and assign training to trainee and trainer")
    public void addTraining(@RequestBody AddTrainingRequest dto) {
        //UPD: Now we return Training object instead of void, because it's needed for tests
        service.addTraining(dto);
    }
}