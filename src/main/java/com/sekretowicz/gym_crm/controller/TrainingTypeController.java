package com.sekretowicz.gym_crm.controller;

import com.sekretowicz.gym_crm.model.TrainingType;
import com.sekretowicz.gym_crm.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/training-types")
@RestController
public class TrainingTypeController {

    @Autowired
    private TrainingTypeService service;

    @GetMapping
    public List<TrainingType> getAll() {
        return service.getAll();
    }
}
