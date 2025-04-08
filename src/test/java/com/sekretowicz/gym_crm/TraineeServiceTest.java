package com.sekretowicz.gym_crm;

import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.service.TraineeService;
import com.sekretowicz.gym_crm.utils.TestDataHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TraineeServiceTest {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TestDataHelper testDataHelper;

    @BeforeEach
    void setup() {
        testDataHelper.seedTrainingTypesIfNeeded();
    }

    @Test
    @DisplayName("Should register trainee with valid data")
    void shouldRegisterTraineeWithValidData() {
        var creds = testDataHelper.createRandomTrainee();
        var trainee = traineeService.getByUsername(creds.getUsername());
        assertNotNull(trainee);
        assertEquals(creds.getUsername(), trainee.getUser().getUsername());
    }

    @Test
    @DisplayName("Should assign trainer to trainee")
    void shouldAssignTrainerToTrainee() {
        Trainee trainee = testDataHelper.createTraineeEntity();
        Trainer trainer = testDataHelper.createTrainerEntity();

        testDataHelper.assignTrainerToTrainee(trainee, trainer);

        Trainee updated = traineeService.getByUsername(trainee.getUser().getUsername());
        assertTrue(updated.getTrainers().contains(trainer));
    }

    @Test
    @DisplayName("Should arrange training for trainee")
    void shouldArrangeTrainingForTrainee() {
        Trainee trainee = testDataHelper.createTraineeEntity();
        Trainer trainer = testDataHelper.createTrainerEntity();
        testDataHelper.assignTrainerToTrainee(trainee, trainer);

        testDataHelper.arrangeTraining(trainee, trainer, "Fitness");

        var trainings = traineeService.getTraineeTrainings(
                trainee.getUser().getUsername(), null, null, null, null);

        assertFalse(trainings.isEmpty());
        assertEquals("Fitness", trainings.get(0).getTrainingType());
    }

    @Test
    @DisplayName("Should set trainee active status")
    void shouldSetTraineeActiveStatus() {
        Trainee trainee = testDataHelper.createTraineeEntity();
        String username = trainee.getUser().getUsername();

        traineeService.setActive(trainee.getId(), false);
        assertFalse(traineeService.getByUsername(username).getUser().isActive());

        traineeService.setActive(trainee.getId(), true);
        assertTrue(traineeService.getByUsername(username).getUser().isActive());
    }

    @Test
    @DisplayName("Should delete trainee and cascade delete trainings")
    void shouldDeleteTraineeAndCascadeTrainings() {
        Trainee trainee = testDataHelper.createTraineeEntity();
        Trainer trainer = testDataHelper.createTrainerEntity();
        testDataHelper.assignTrainerToTrainee(trainee, trainer);
        testDataHelper.arrangeTraining(trainee, trainer, "Fitness");

        String username = trainee.getUser().getUsername();
        traineeService.deleteByUsername(username);

        assertThrows(RuntimeException.class, () -> traineeService.getByUsername(username));
    }

    @Test
    @DisplayName("Should return filtered trainings for trainee")
    void shouldReturnFilteredTrainings() {
        Trainee trainee = testDataHelper.createTraineeEntity();
        Trainer trainer = testDataHelper.createTrainerEntity();
        testDataHelper.assignTrainerToTrainee(trainee, trainer);
        testDataHelper.arrangeTraining(trainee, trainer, "Fitness");

        var result = traineeService.getTraineeTrainings(
                trainee.getUser().getUsername(), null, null,
                trainer.getUser().getUsername(), "Fitness"
        );

        assertEquals(1, result.size());
        assertEquals("Fitness", result.get(0).getTrainingType());
    }
}