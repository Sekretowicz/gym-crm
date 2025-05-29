package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.feign.WorkloadClient;
import com.sekretowicz.gym_crm.service.TraineeService;
import com.sekretowicz.gym_crm.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    //Just testing it works
    @Autowired
    private WorkloadClient client;

    @GetMapping("/hello")
    public void hello() {
        client.sayHello();
    }

    //We don't use pages at all
    /*
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

     */
}