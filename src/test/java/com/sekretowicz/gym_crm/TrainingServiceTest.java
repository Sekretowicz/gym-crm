package com.sekretowicz.gym_crm;

import static org.mockito.Mockito.*;

import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.model.TrainingType;
import com.sekretowicz.gym_crm.repo.TrainingRepo;
import com.sekretowicz.gym_crm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Date;

class TrainingServiceTest {
    @Mock
    private TrainingRepo trainingRepo;

    @InjectMocks
    private TrainingService trainingService;

    private Training testTraining;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testTraining = new Training();
        testTraining.setId(1L);
        testTraining.setTrainingName("Morning Workout");
        testTraining.setTrainingDate(new Date());
        testTraining.setTrainingDuration(60);
        testTraining.setTrainer(new Trainer());
        testTraining.setTrainee(new Trainee());
        testTraining.setTrainingType(new TrainingType());
    }

    @Test
    void addTraining_ShouldSaveTraining() {
        when(trainingRepo.save(any(Training.class))).thenReturn(testTraining);
        trainingService.addTraining(testTraining);
        verify(trainingRepo, times(1)).save(testTraining);
    }
}
