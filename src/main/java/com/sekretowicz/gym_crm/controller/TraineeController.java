package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.dto.common.SetActiveRequest;
import com.sekretowicz.gym_crm.dto.trainee.TraineeProfileResponse;
import com.sekretowicz.gym_crm.dto.trainee.TraineeRegistrationRequest;
import com.sekretowicz.gym_crm.dto.trainee.TraineeUpdateRequest;
import com.sekretowicz.gym_crm.dto.trainee.UpdateTraineeTrainersRequest;
import com.sekretowicz.gym_crm.dto.trainer.TrainerShortDto;
import com.sekretowicz.gym_crm.dto.training.TrainingResponse;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.repo.TraineeRepo;
import com.sekretowicz.gym_crm.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trainees")
@Tag(name = "Trainee Controller", description = "Trainee operations")
public class TraineeController {
    @Autowired
    private TraineeService service;

    //1. Trainee Registration (POST method)
    @PostMapping("/register")
    @Operation(summary = "Register a new trainee", description = "Register a new trainee and return credentials")
    public UserCredentials register (@RequestBody TraineeRegistrationRequest request) {
        return service.register(request);
    }

    //5. Get Trainee Profile (GET method)
    @Operation(summary = "Get Trainee Profile", description = "Retrieve the profile of a trainee by username")
    @GetMapping("/{username}")
    @ResponseBody
    public ResponseEntity<TraineeProfileResponse> getTraineeProfile(@PathVariable String username) {
        return ResponseEntity.ok(service.getTraineeProfile(username));
    }

    //6. Update Trainee Profile (PUT method)
    @PutMapping("/profile")
    @Operation(summary = "Update Trainee Profile", description = "Update the profile of a trainee")
    public TraineeProfileResponse updateTraineeProfile(@RequestBody TraineeUpdateRequest request) {
        return service.updateTraineeProfile(request);
    }

    //7. Delete Trainee Profile (DELETE method)
    @DeleteMapping("/{username}")
    @Operation(summary = "Delete Trainee Profile", description = "Delete the profile of a trainee by username")
    public void deleteTraineeProfile(@PathVariable String username) {
        service.deleteByUsername(username);
    }

    //10. Get not assigned on trainee active trainers. (GET method)
    @GetMapping("/{username}/unassigned-trainers")
    @Operation(summary = "Get Unassigned Trainers", description = "Get a list of trainers not assigned to the trainee")
    public List<TrainerShortDto> getUnassignedTrainers(@PathVariable String username) {
        return service.getUnassignedTrainers(username);
    }

    //11. Update Trainee's Trainer List (PUT method)
    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update Trainer List", description = "Update the list of trainers assigned to the trainee")
    public List<TrainerShortDto> updateTrainerList(@PathVariable String username,
                                              @RequestBody UpdateTraineeTrainersRequest request) {
        return service.updateTrainerList(username, request);
    }

    //12. Get Trainee Trainings List (GET method)
    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get Trainee Trainings", description = "Get a list of trainings for the trainee")
    public List<TrainingResponse> getTrainings(@PathVariable String username,
                                               @RequestParam(required = false) LocalDate fromDate,
                                               @RequestParam(required = false) LocalDate toDate,
                                               @RequestParam(required = false) String trainerName,
                                               @RequestParam(required = false) String trainingType) {
        return service.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
    }

    //15. Activate/De-Activate Trainee (PATCH method)
    @PatchMapping("/status")
    @Operation(summary = "Set Active Status", description = "Activate or deactivate a trainee")
    public void setActive(@RequestBody SetActiveRequest request) {
        service.setActive(request);
    }

}
