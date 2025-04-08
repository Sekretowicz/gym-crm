package com.sekretowicz.gym_crm;

import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.service.TrainerService;
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
class TrainerServiceTest {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TestDataHelper testDataHelper;

    @BeforeEach
    void setup() {
        testDataHelper.seedTrainingTypesIfNeeded();
    }

    @Test
    @DisplayName("Should register trainer with valid data")
    void shouldRegisterTrainerWithValidData() {
        var creds = testDataHelper.createRandomTrainer();
        var trainer = trainerService.getByUsername(creds.getUsername());
        assertNotNull(trainer);
        assertEquals(creds.getUsername(), trainer.getUser().getUsername());
    }

    @Test
    @DisplayName("Should return trainer by username")
    void shouldReturnTrainerByUsername() {
        Trainer trainer = testDataHelper.createTrainerEntity();
        Trainer found = trainerService.getByUsername(trainer.getUser().getUsername());
        assertNotNull(found);
        assertEquals(trainer.getId(), found.getId());
    }

    @Test
    @DisplayName("Should set trainer active status")
    void shouldSetTrainerActiveStatus() {
        Trainer trainer = testDataHelper.createTrainerEntity();
        String username = trainer.getUser().getUsername();

        trainerService.setActive(trainer.getId(), false);
        assertFalse(trainerService.getByUsername(username).getUser().isActive());

        trainerService.setActive(trainer.getId(), true);
        assertTrue(trainerService.getByUsername(username).getUser().isActive());
    }

    @Test
    @DisplayName("Should update trainer profile")
    void shouldUpdateTrainerProfile() {
        Trainer trainer = testDataHelper.createTrainerEntity();
        var user = trainer.getUser();

        user.setFirstName("UpdatedName");
        user.setLastName("UpdatedLast");
        user.setActive(false);
        trainer.setUser(user);

        trainerService.update(trainer);

        Trainer updated = trainerService.getByUsername(user.getUsername());
        assertEquals("UpdatedName", updated.getUser().getFirstName());
        assertFalse(updated.getUser().isActive());
    }

    @Test
    @DisplayName("Should return filtered trainings for trainer")
    void shouldReturnFilteredTrainings() {
        Trainer trainer = testDataHelper.createTrainerEntity();
        Trainee trainee = testDataHelper.createTraineeEntity();
        testDataHelper.assignTrainerToTrainee(trainee, trainer);
        testDataHelper.arrangeTraining(trainee, trainer, "Fitness");

        var result = trainerService.getTrainerTrainings(
                trainer.getUser().getUsername(), null, null,
                trainee.getUser().getUsername()
        );

        assertEquals(1, result.size());
        assertEquals("Fitness", result.get(0).getTrainingType());
    }
}
