package com.sekretowicz.gym_crm.service;

import com.github.javafaker.Faker;
import com.sekretowicz.gym_crm.dao.TrainerDao;
import com.sekretowicz.gym_crm.dao.UserDao;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private TrainerService trainerService;

    private Faker faker;
    private Trainer trainer;
    private User user;

    @BeforeEach
    void setUp() {
        faker = new Faker();

        user = new User(1L, faker.name().firstName(), faker.name().lastName(), false);
        trainer = new Trainer(1L, user.getId(), 2L); // userId = 1, trainingTypeId = 2
    }

    @Test
    void shouldCreateTrainer() {
        trainerService.create(trainer);
        verify(trainerDao, times(1)).save(trainer);
    }

    @Test
    void shouldReturnTrainerById() {
        when(trainerDao.getById(1L)).thenReturn(trainer);

        Trainer result = trainerService.getById(1L);

        assertNotNull(result);
        assertEquals(2L, result.getTrainingTypeId());
        verify(trainerDao, times(1)).getById(1L);
    }

    @Test
    void shouldReturnAllTrainers() {
        when(trainerDao.getAll()).thenReturn(List.of(trainer));

        List<Trainer> trainers = trainerService.getAll();

        assertFalse(trainers.isEmpty());
        assertEquals(1, trainers.size());
        verify(trainerDao, times(1)).getAll();
    }

    @Test
    void shouldReturnFullNameOfTrainer() {
        when(trainerDao.getById(1L)).thenReturn(trainer);
        when(userDao.getById(1L)).thenReturn(user);

        String fullName = trainerService.getTrainerFullName(1L);
        assertEquals(user.getFirstName() + " " + user.getLastName(), fullName);
    }

    @Test
    void shouldThrowExceptionWhenTrainerNotFound() {
        when(trainerDao.getById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> trainerService.getTrainerFullName(1L));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(trainerDao.getById(1L)).thenReturn(trainer);
        when(userDao.getById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> trainerService.getTrainerFullName(1L));
    }
}