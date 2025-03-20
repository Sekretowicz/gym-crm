package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.dto.TrainingDto;
import com.sekretowicz.gym_crm.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    @Autowired
    private TrainingService service;

    //14. Add Training (POST method)
    @PostMapping("/")
    public void addTraining(@RequestBody TrainingDto dto) {
        service.addTraining(dto);
    }
}
