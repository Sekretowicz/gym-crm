package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.model.TrainingType;
import com.sekretowicz.gym_crm.repo.TrainingTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeService {
    @Autowired
    private TrainingTypeRepo trainingTypeRepo;

    public TrainingType getByName(String name) {
        return trainingTypeRepo.findByTrainingTypeName(name);
    }

    public TrainingType getById(Long id) {
        return trainingTypeRepo.findById(id).orElseThrow(() -> new RuntimeException("TrainingType not found"));
    }

    public List<TrainingType> getAll() {
        return trainingTypeRepo.findAll();
    }
}
