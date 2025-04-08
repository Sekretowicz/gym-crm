package com.sekretowicz.gym_crm;

import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.repo.TrainingTypeRepo;
import com.sekretowicz.gym_crm.service.TrainingService;
import com.sekretowicz.gym_crm.utils.TestDataHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TrainingServiceTest {

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TestDataHelper testDataHelper;

    @Autowired
    private TrainingTypeRepo trainingTypeRepo;

    private Trainee trainee;
    private Trainer trainer;

    @BeforeEach
    void setup() {
        testDataHelper.seedTrainingTypesIfNeeded();
        trainee = testDataHelper.createTraineeEntity();
        trainer = testDataHelper.createTrainerEntity();
        testDataHelper.assignTrainerToTrainee(trainee, trainer);
    }

    @Test
    @DisplayName("Should add training successfully")
    void shouldAddTraining() {
        Training training = new Training();
        training.setTrainingName("Morning session");
        training.setTrainingDate(LocalDate.now());
        training.setTrainingDuration(45);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainer.getSpecialization().equals("Fitness") ?
                trainingTypeRepo.findByTrainingTypeName("Fitness") : null);

        Training saved = trainingService.addTraining(training);
        assertNotNull(saved.getId());
        assertEquals("Morning session", saved.getTrainingName());
    }

    @Test
    @DisplayName("Should return trainee trainings with filters")
    void shouldReturnTraineeTrainingsWithFilters() {
        testDataHelper.arrangeTraining(trainee, trainer, "Fitness");

        var trainings = trainingService.getTraineeTrainings(
                trainee.getUser().getUsername(),
                null, null,
                trainer.getUser().getUsername(),
                "Fitness"
        );

        assertEquals(1, trainings.size());
        assertEquals("Fitness", trainings.get(0).getTrainingType().getTrainingTypeName());
    }

    @Test
    @DisplayName("Should return trainer trainings with filters")
    void shouldReturnTrainerTrainingsWithFilters() {
        testDataHelper.arrangeTraining(trainee, trainer, "Fitness");

        var trainings = trainingService.getTrainerTrainings(
                trainer.getUser().getUsername(),
                null, null,
                trainee.getUser().getUsername()
        );

        assertEquals(1, trainings.size());
        assertEquals("Fitness", trainings.get(0).getTrainingType().getTrainingTypeName());
    }

    @Test
    @DisplayName("Should return empty list if filters do not match")
    void shouldReturnEmptyListWhenNoMatch() {
        var trainings = trainingService.getTraineeTrainings(
                trainee.getUser().getUsername(),
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(10),
                null,
                "Yoga"
        );

        assertTrue(trainings.isEmpty());
    }
}
