package com.sekretowicz.gym_crm.utils;

import com.github.javafaker.Faker;
import com.sekretowicz.gym_crm.dto.trainee.TraineeRegistrationRequest;
import com.sekretowicz.gym_crm.dto.trainer.TrainerRegistrationRequest;
import com.sekretowicz.gym_crm.dto_legacy.ShortTraineeDto;
import com.sekretowicz.gym_crm.dto_legacy.ShortTrainerDto;
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

@Component
public class TestDataHelper {

    @Autowired private UserService userService;
    @Autowired private TraineeService traineeService;
    @Autowired private TrainerService trainerService;
    @Autowired private TrainingService trainingService;
    @Autowired private TrainingTypeRepo trainingTypeRepo;

    public static final String DEFAULT_TRAINING_TYPE = "Fitness";

    private final Faker faker = new Faker();

    public void seedTrainingTypesIfNeeded() {
        if (trainingTypeRepo.count() == 0) {
            trainingTypeRepo.save(new TrainingType(DEFAULT_TRAINING_TYPE));
        }
    }

    //Reusing ShortTraineeDto to store trainee data
    public ShortTraineeDto createRandomTrainee() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String address = faker.address().fullAddress();
        LocalDate dateOfBirth = LocalDate.of(
                faker.number().numberBetween(1980, 2000),
                faker.number().numberBetween(1, 12),
                faker.number().numberBetween(1, 28)
        );

        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setAddress(address);
        request.setDateOfBirth(dateOfBirth);
        UserCredentials creds = traineeService.register(request);

        return new ShortTraineeDto(
                creds.getUsername(),
                firstName,
                lastName,
                dateOfBirth,
                address,
                true
        );
    }

    //Reusing ShortTrainerDto to store trainer data
    public ShortTrainerDto createRandomTrainer() {
        TrainingType trainingType = trainingTypeRepo.findByTrainingTypeName(DEFAULT_TRAINING_TYPE);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        TrainerRegistrationRequest request = new TrainerRegistrationRequest(firstName, lastName, trainingType.getId());
        UserCredentials creds = trainerService.register(request);

        return new ShortTrainerDto(creds.getUsername(), firstName, lastName, trainingType, true);
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
        training.setTrainingDuration(60);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingTypeRepo.findByTrainingTypeName(typeName));
        trainingService.addTraining(training);
    }
}
