package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.dto.common.SetActiveRequest;
import com.sekretowicz.gym_crm.dto.trainer.TrainerProfileResponse;
import com.sekretowicz.gym_crm.dto.trainer.TrainerRegistrationRequest;
import com.sekretowicz.gym_crm.dto.trainer.TrainerUpdateRequest;
import com.sekretowicz.gym_crm.dto.training.TrainingResponse;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trainers")
@Tag(name = "Trainer Controller", description = "Trainer operations")
public class TrainerController {
    @Autowired
    private TrainerService service;

    //2. Trainer Registration (POST method)
    @PostMapping("/register")
    @Operation(summary = "Register a new trainer", description = "Register a new trainer and return credentials")
    public UserCredentials register (@RequestBody TrainerRegistrationRequest request) {
        return service.register(request);
    }

    //8. Get Trainer Profile (GET method)
    @GetMapping("/{username}")
    @Operation(summary = "Get trainer profile", description = "Fetch profile information of a trainer")
    public TrainerProfileResponse getTrainerProfile(@PathVariable String username) {
        return service.getTrainerProfile(username);
    }

    //9. Update Trainer Profile (PUT method)
    @PutMapping("/profile")
    @Operation(summary = "Update trainer profile", description = "Update trainer details (specialization is read-only)")
    public TrainerProfileResponse updateTrainerProfile(@RequestBody TrainerUpdateRequest dto) {
        return service.updateTrainerProfile(dto);
    }

    //13. Get Trainer Trainings List (GET method)
    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainer's trainings", description = "Fetch all trainings conducted by the trainer with optional filters")
    public List<TrainingResponse> getTrainings (@PathVariable String username,
                                                @RequestParam(required = false) LocalDate fromDate,
                                                @RequestParam(required = false) LocalDate toDate,
                                                @RequestParam(required = false) String traineeName) {
        return service.getTrainerTrainings(username, fromDate, toDate, traineeName);
    }

    //16. Activate/De-Activate Trainer (PATCH method)
    @PatchMapping("/status")
    @Operation(summary = "Activate or deactivate trainer", description = "Set active status for trainer")
    public void setActive(@RequestBody SetActiveRequest request) {
        service.setActive(request);
    }
}
