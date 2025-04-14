package com.sekretowicz.gym_crm.utils;

import com.github.javafaker.Faker;
import com.sekretowicz.gym_crm.dto.trainee.TraineeRegistrationRequest;
import com.sekretowicz.gym_crm.dto.trainer.TrainerRegistrationRequest;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.model.TrainingType;
import com.sekretowicz.gym_crm.repo.TrainingTypeRepo;
import com.sekretowicz.gym_crm.service.TraineeService;
import com.sekretowicz.gym_crm.service.TrainerService;
import com.sekretowicz.gym_crm.service.TrainingService;
import com.sekretowicz.gym_crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TestDataHelper {

    @Autowired private UserService userService;
    @Autowired private TraineeService traineeService;
    @Autowired private TrainerService trainerService;
    @Autowired private TrainingService trainingService;
    @Autowired private TrainingTypeRepo trainingTypeRepo;

    private final Faker faker = new Faker();

    public void seedTrainingTypesIfNeeded() {
        if (trainingTypeRepo.count() == 0) {
            trainingTypeRepo.saveAll(List.of(
                    new TrainingType("Fitness"),
                    new TrainingType("Yoga"),
                    new TrainingType("Cardio")
            ));
        }
    }

    public UserCredentials createRandomTrainee() {
        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        request.setFirstName(faker.name().firstName());
        request.setLastName(faker.name().lastName());
        request.setAddress(faker.address().fullAddress());
        request.setDateOfBirth(LocalDate.of(
                faker.number().numberBetween(1980, 2000),
                faker.number().numberBetween(1, 12),
                faker.number().numberBetween(1, 28))
        );
        return traineeService.register(request);
    }

    public Trainee createTraineeEntity() {
        UserCredentials creds = createRandomTrainee();
        return traineeService.getByUsername(creds.getUsername());
    }

    public UserCredentials createRandomTrainer() {
        TrainingType trainingType = trainingTypeRepo.findByTrainingTypeName("Fitness");
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        request.setFirstName(faker.name().firstName());
        request.setLastName(faker.name().lastName());
        request.setSpecId(trainingType.getId());
        return trainerService.register(request);
    }

    public Trainer createTrainerEntity() {
        UserCredentials creds = createRandomTrainer();
        return trainerService.getByUsername(creds.getUsername());
    }

    public void assignTrainerToTrainee(String traineeUsername, String trainerUsername) {
        Trainee trainee = traineeService.getByUsername(traineeUsername);
        Trainer trainer = trainerService.getByUsername(trainerUsername);
        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);
        traineeService.update(trainee);
        trainerService.update(trainer);
    }

    public void assignTrainerToTrainee(Trainee trainee, Trainer trainer) {
        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);
        traineeService.update(trainee);
        trainerService.update(trainer);
    }

    public void arrangeTraining(String traineeUsername, String trainerUsername, String typeName) {
        Training training = new Training();
        training.setTrainingName("Session with " + trainerUsername);
        training.setTrainingDate(LocalDate.now());
        training.setTrainingDuration(60);
        training.setTrainee(traineeService.getByUsername(traineeUsername));
        training.setTrainer(trainerService.getByUsername(trainerUsername));
        training.setTrainingType(trainingTypeRepo.findByTrainingTypeName(typeName));
        trainingService.addTraining(training);
    }

    public void arrangeTraining(Trainee trainee, Trainer trainer, String typeName) {
        Training training = new Training();
        training.setTrainingName("Auto session");
        training.setTrainingDate(LocalDate.now());
        training.setTrainingDuration(45);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingTypeRepo.findByTrainingTypeName(typeName));
        trainingService.addTraining(training);
    }

    public TraineeRegistrationRequest createInvalidTraineeRequest_MissingFirstName() {
        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        request.setLastName(faker.name().lastName());
        request.setAddress(faker.address().fullAddress());
        request.setDateOfBirth(LocalDate.of(1990, 5, 20));
        return request;
    }

    public TraineeRegistrationRequest createInvalidTraineeRequest_MissingLastName() {
        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        request.setFirstName(faker.name().firstName());
        request.setAddress(faker.address().fullAddress());
        request.setDateOfBirth(LocalDate.of(1990, 5, 20));
        return request;
    }

    public TrainerRegistrationRequest createInvalidTrainerRequest_MissingFirstName() {
        TrainingType trainingType = trainingTypeRepo.findByTrainingTypeName("Fitness");
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        request.setLastName(faker.name().lastName());
        request.setSpecId(trainingType.getId());
        return request;
    }

    public TrainerRegistrationRequest createInvalidTrainerRequest_MissingSpecialization() {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        request.setFirstName(faker.name().firstName());
        request.setLastName(faker.name().lastName());
        return request;
    }
}
