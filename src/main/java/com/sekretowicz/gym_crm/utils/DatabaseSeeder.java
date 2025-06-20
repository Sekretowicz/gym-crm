package com.sekretowicz.gym_crm.utils;

import com.github.javafaker.Faker;
import com.sekretowicz.gym_crm.dto.trainee.TraineeRegistrationRequest;
import com.sekretowicz.gym_crm.dto.trainer.TrainerRegistrationRequest;
import com.sekretowicz.gym_crm.dto.training.AddTrainingRequest;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.model.TrainingType;
import com.sekretowicz.gym_crm.repo.TrainingTypeRepo;
import com.sekretowicz.gym_crm.service.TraineeService;
import com.sekretowicz.gym_crm.service.TrainerService;
import com.sekretowicz.gym_crm.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;

@Component
public class DatabaseSeeder implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private TrainingTypeRepo trainingTypeRepo;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainingService trainingService;

    private final Faker faker = new Faker(new Locale("en"));

    public void onApplicationEvent(ApplicationReadyEvent event){
        System.out.println("I'm ready! Waiting for 30 seconds before seeding the database...");
        try {
            Thread.sleep(30*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 1. Training Types
        TrainingType fitness = new TrainingType();
        fitness.setTrainingTypeName("Fitness");
        trainingTypeRepo.save(fitness);

        TrainingType boxing = new TrainingType();
        boxing.setTrainingTypeName("Boxing");
        trainingTypeRepo.save(boxing);

        // 2. Register Trainers
        TrainerRegistrationRequest trainer1Req = new TrainerRegistrationRequest();
        trainer1Req.setFirstName(faker.name().firstName());
        trainer1Req.setLastName(faker.name().lastName());
        trainer1Req.setSpecId(fitness.getId());
        UserCredentials trainer1 = trainerService.register(trainer1Req);
        //Print credentials
        System.out.println("\nTrainer 1:");
        System.out.println("Username: " + trainer1.getUsername());
        System.out.println("Password: " + trainer1.getPassword());
        System.out.println("Token: " + trainer1.getToken() + "\n");

        TrainerRegistrationRequest trainer2Req = new TrainerRegistrationRequest();
        trainer2Req.setFirstName(faker.name().firstName());
        trainer2Req.setLastName(faker.name().lastName());
        trainer2Req.setSpecId(boxing.getId());
        UserCredentials trainer2 = trainerService.register(trainer2Req);

        // 3. Register Trainees
        TraineeRegistrationRequest trainee1Req = new TraineeRegistrationRequest();
        trainee1Req.setFirstName(faker.name().firstName());
        trainee1Req.setLastName(faker.name().lastName());
        trainee1Req.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee1Req.setAddress(faker.address().fullAddress());
        UserCredentials trainee1 = traineeService.register(trainee1Req);

        TraineeRegistrationRequest trainee2Req = new TraineeRegistrationRequest();
        trainee2Req.setFirstName(faker.name().firstName());
        trainee2Req.setLastName(faker.name().lastName());
        trainee2Req.setDateOfBirth(LocalDate.of(1999, 5, 15));
        trainee2Req.setAddress(faker.address().fullAddress());
        UserCredentials trainee2 = traineeService.register(trainee2Req);

        // 4. Trainings
        AddTrainingRequest training1 = new AddTrainingRequest();
        training1.setTraineeUsername(trainee1.getUsername());
        training1.setTrainerUsername(trainer1.getUsername());
        training1.setTrainingName("Cardio " + faker.number().digit());
        training1.setTrainingDate(LocalDate.now().minusDays(3));
        training1.setTrainingDuration(60);
        trainingService.addTraining(training1);

        AddTrainingRequest training2 = new AddTrainingRequest();
        training2.setTraineeUsername(trainee2.getUsername());
        training2.setTrainerUsername(trainer2.getUsername());
        training2.setTrainingName("Boxing Basics " + faker.number().digit());
        training2.setTrainingDate(LocalDate.now().minusDays(1));
        training2.setTrainingDuration(45);
        trainingService.addTraining(training2);
    }
}