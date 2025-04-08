package com.sekretowicz.gym_crm;

import com.github.javafaker.Faker;
import com.sekretowicz.gym_crm.dto.trainee.TraineeRegistrationRequest;
import com.sekretowicz.gym_crm.dto.trainer.TrainerRegistrationRequest;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.service.TraineeService;
import com.sekretowicz.gym_crm.service.TrainerService;
import com.sekretowicz.gym_crm.service.TrainingService;
import com.sekretowicz.gym_crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class DatabaseSeeder {
    //All services
    @Autowired
    private UserService userService;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainingService trainingService;

    Faker faker = new Faker();

    private UserCredentials createRandomTrainee() {
        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        request.setFirstName(faker.name().firstName());
        request.setLastName(faker.name().lastName());
        request.setAddress(faker.address().fullAddress());
        request.setDateOfBirth(LocalDate.of(faker.number().numberBetween(1980, 2000), faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28)));
        return traineeService.register(request);
    }

    private UserCredentials createRandomTrainer() {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        request.setFirstName(faker.name().firstName());
        request.setLastName(faker.name().lastName());
        request.setSpecialization("Fitness");
        return trainerService.register(request);
    }

    private void assignTrainerToTrainee(String traineeName, String trainerName) {
        Trainee trainee = traineeService.getByUsername(traineeName);
        Trainer trainer = trainerService.getByUsername(trainerName);
        if (trainee != null && trainer != null) {
            trainee.getTrainers().add(trainer);
            trainer.getTrainees().add(trainee);
            traineeService.update(trainee);
            trainerService.update(trainer);
        }
    }

    private void arrangeTraining(String traineeName, String trainerName) {
        Training training = new Training();
        training.setTrainingName("Training with " + trainerName);
        training.setTrainingDate(LocalDate.now());

    }
}
