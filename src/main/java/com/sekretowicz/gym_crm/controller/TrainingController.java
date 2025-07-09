package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.dto.training.AddTrainingRequest;
import com.sekretowicz.gym_crm.dto.training.TrainingResponse;
import com.sekretowicz.gym_crm.dto.training.VerboseTrainingResponse;
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
    //UPD: Now we return VerboseTrainingResponse, which contains all possible information about training.
    @PostMapping
    @Operation(summary = "Add new training", description = "Create and assign training to trainee and trainer")
    public VerboseTrainingResponse addTraining(@RequestBody AddTrainingRequest dto) {
        return service.addTraining(dto);
    }

    //Added for Cucumber task
    //How can we know training ID? Let's assume we know it somehow. Anyway, we must test it.
    @DeleteMapping("/{id}")
    public void cancelTraining(@PathVariable Long id) {
        service.deleteTraining(id);
    }
}