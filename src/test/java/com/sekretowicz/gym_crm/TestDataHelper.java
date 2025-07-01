package com.sekretowicz.gym_crm;

import com.sekretowicz.gym_crm.dto.trainer.TrainerRegistrationRequest;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.model.TrainingType;
import com.sekretowicz.gym_crm.repo.TrainingTypeRepo;
import com.sekretowicz.gym_crm.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataHelper {

    @Autowired
    TrainingTypeRepo trainingTypeRepo;
    @Autowired
    TrainerService trainerService;

    public void initializeTrainingTypes() {
        //Just add a couple of hardcoded training types via repo
        //We'll just use trainingType ID=0 anywhere we need a training type
        TrainingType fitness = new TrainingType("Fitness");
        trainingTypeRepo.save(fitness);
    }

    public void registerHardcodedTrainer() {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest(
                "John",
                "Doe",
                0L // Assuming 0 is the ID for the "Fitness" training type
        );
        UserCredentials credentials = trainerService.register(request);
    }
}