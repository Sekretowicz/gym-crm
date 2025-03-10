package com.sekretowicz.gym_crm.utils;

import com.github.javafaker.Faker;
import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.model.User;
import com.sekretowicz.gym_crm.service.TraineeService;
import com.sekretowicz.gym_crm.service.TrainerService;
import com.sekretowicz.gym_crm.service.TrainingService;
import com.sekretowicz.gym_crm.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class DatabaseSeeder {
    private final UserService userService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Autowired
    public DatabaseSeeder(UserService userService, TrainerService trainerService,
                          TraineeService traineeService, TrainingService trainingService) {
        this.userService = userService;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    @Transactional
    public void seedDatabase() {
        // Создание тренеров
        List<Trainer> trainers = IntStream.range(0, 5)
                .mapToObj(i -> createTrainer())
                .toList();

        // Создание учеников
        List<Trainee> trainees = IntStream.range(0, 10)
                .mapToObj(i -> createTrainee())
                .toList();

        // Назначение тренеров ученикам
        trainees.forEach(trainee -> {
            Trainer trainer = trainers.get(random.nextInt(trainers.size()));
            trainee.getTrainers().add(trainer);
            traineeService.update(trainee);
        });

        // Создание тренировок
        trainees.forEach(trainee -> {
            Trainer trainer = trainee.getTrainers().get(0); // Берем первого тренера
            createTraining(trainee, trainer);
        });
    }

    private Trainer createTrainer() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userService.create(user);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization("General Fitness");
        trainerService.create(trainer);
        return trainer;
    }

    private Trainee createTrainee() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userService.create(user);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
        traineeService.create(trainee);
        return trainee;
    }

    private void createTraining(Trainee trainee, Trainer trainer) {
        Training training = new Training();
        training.setTrainingName("Fitness");
        training.setTrainingDate(new java.util.Date());
        training.setTrainingDuration(random.nextInt(30, 120));
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        trainingService.addTraining(training);
    }
}