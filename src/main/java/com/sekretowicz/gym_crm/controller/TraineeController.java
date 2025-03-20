package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.dto.TraineeDto;
import com.sekretowicz.gym_crm.dto.TrainerDto;
import com.sekretowicz.gym_crm.dto.TrainingDto;
import com.sekretowicz.gym_crm.dto.UserCredentials;
import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trainees")
public class TraineeController {
    @Autowired
    private TraineeService service;

    //1. Trainee Registration (POST method)
    @PostMapping("/register")
    public UserCredentials register (@RequestBody TraineeDto dto) {
        return service.register(dto);
    }

    //5. Get Trainee Profile (GET method)
    @GetMapping("/{username}")
    public TraineeDto getTraineeProfile(@PathVariable String username) {
        return service.getTraineeProfile(username);
    }

    //6. Update Trainee Profile (PUT method)
    @PutMapping("/")
    public TraineeDto updateTraineeProfile(@RequestBody TraineeDto dto) {
        return service.updateTraineeProfile(dto);
    }

    //7. Delete Trainee Profile (DELETE method)
    @DeleteMapping("/{username}")
    public void deleteTraineeProfile(@PathVariable String username) {
        //TODO: Возможно, стоит бросить исключение, если такого юзера нет. Тогда метод для удаления надо сделать boolean
        service.deleteByUsername(username);
    }

    //10. Get not assigned on trainee active trainers. (GET method)
    @GetMapping("/{username}/unassigned-trainers")
    public List<TrainerDto> getUnassignedTrainers(@PathVariable String username) {
        return service.getUnassignedTrainers(username).stream()
                .map(trainer -> new TrainerDto(trainer, false))
                .toList();
    }

    //11. Update Trainee's Trainer List (PUT method)
    @PutMapping("/{username}")
    public List<TrainerDto> updateTrainerList(@PathVariable String username, @RequestBody TraineeDto dto) {
        return service.updateTrainerList(dto);
    }

    //12. Get Trainee Trainings List (GET method)
    @GetMapping("/{username}/trainings")
    public List<TrainingDto> getTrainings(@PathVariable String username,
                                          @RequestParam(required = false) LocalDate fromDate,
                                          @RequestParam(required = false) LocalDate toDate,
                                          @RequestParam(required = false) String trainerName,
                                          @RequestParam(required = false) String trainingType) {
        List<Training> trainings = service.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);

        return trainings.stream().map(TrainingDto::new).toList();
    }

    //15. Activate/De-Activate Trainee (PATCH method)
    @PatchMapping("/{username}")
    public void setActive(@PathVariable String username, @RequestParam boolean isActive) {
        service.setActive(username, isActive);
    }
}
