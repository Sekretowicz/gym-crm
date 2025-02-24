package com.sekretowicz.gym_crm.service;

import com.github.javafaker.Faker;
import com.sekretowicz.gym_crm.dao.TrainingDao;
import com.sekretowicz.gym_crm.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    private Faker faker;
    private Training training;

    @BeforeEach
    void setUp() {
        faker = new Faker();

        training = new Training(1L, 10L, 20L, "Cardio Training", 5L, LocalDate.now(), 60);
    }

    @Test
    void shouldCreateTraining() {
        trainingService.create(training);
        verify(trainingDao, times(1)).save(training);
    }

    @Test
    void shouldReturnTrainingById() {
        when(trainingDao.getById(1L)).thenReturn(training);

        Training result = trainingService.getById(1L);

        assertNotNull(result);
        assertEquals("Cardio Training", result.getName());
        assertEquals(10L, result.getTraineeId());
        assertEquals(20L, result.getTrainerId());
        assertEquals(5L, result.getTrainingTypeId());
        assertEquals(60, result.getDuration());

        verify(trainingDao, times(1)).getById(1L);
    }

    @Test
    void shouldReturnAllTrainings() {
        when(trainingDao.getAll()).thenReturn(List.of(training));

        List<Training> trainings = trainingService.getAll();

        assertFalse(trainings.isEmpty());
        assertEquals(1, trainings.size());
        verify(trainingDao, times(1)).getAll();
    }
}