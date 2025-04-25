package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.dto.training.TrainingResponse;
import com.sekretowicz.gym_crm.service.TraineeService;
import com.sekretowicz.gym_crm.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);

            List<TrainingResponse> trainings;
            String role;

            try {
                trainings = traineeService.getTraineeTrainings(username, null, null, null, null);
                role = "trainee";
            } catch (Exception e) {
                trainings = trainerService.getTrainerTrainings(username, null, null, null);
                role = "trainer";
            }

            model.addAttribute("role", role);
            model.addAttribute("trainings", trainings);
        }

        return "home";
    }
}