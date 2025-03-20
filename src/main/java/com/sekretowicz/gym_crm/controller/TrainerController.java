package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.dto.TrainerDto;
import com.sekretowicz.gym_crm.dto.TrainingDto;
import com.sekretowicz.gym_crm.dto.UserCredentials;
import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trainers")
public class TrainerController {
    @Autowired
    private TrainerService service;

    //2. Trainer Registration (POST method)
    @PostMapping("/register")
    public UserCredentials register (@RequestBody TrainerDto dto) {
        return service.register(dto);
    }

    //8. Get Trainer Profile (GET method)
    @GetMapping("/{username}")
    public TrainerDto getTrainerProfile(@PathVariable String username) {
        return service.getTrainerProfile(username);
    }
    
    //9. Update Trainer Profile (PUT method)
    @PutMapping("/")
    public TrainerDto updateTrainerProfile(@RequestBody TrainerDto dto) {
        return service.updateTrainerProfile(dto);
    }

    //13. Get Trainer Trainings List (GET method)
    @GetMapping("/{username}/trainings")
    public List<TrainingDto> getTrainings (@PathVariable String username,
                                           @RequestParam(required = false) LocalDate fromDate,
                                           @RequestParam(required = false) LocalDate toDate,
                                           @RequestParam(required = false) String traineeName) {
        List<Training> trainings = service.getTrainerTrainings(username, fromDate, toDate, traineeName);

        return trainings.stream()
                .map(TrainingDto::new)
                .toList();
    }

    //16. Activate/De-Activate Trainer (PATCH method)
    @PatchMapping("/{username}")
    public void setActive(@PathVariable String username, @RequestParam boolean isActive) {
        service.setActive(username, isActive);
    }
}
