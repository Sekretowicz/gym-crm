package com.sekretowicz.gym_crm.service;

import com.github.javafaker.Faker;
import com.sekretowicz.gym_crm.dao.TraineeDao;
import com.sekretowicz.gym_crm.dao.UserDao;
import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private TraineeService traineeService;

    private Faker faker;
    private Trainee trainee;
    private User user;

    @BeforeEach
    void setUp() {
        faker = new Faker();

        user = new User(1L, faker.name().firstName(), faker.name().lastName(), true);
        trainee = new Trainee(1L, LocalDate.of(1995, 5, 10), faker.address().fullAddress(), user.getId());
    }

    @Test
    void shouldCreateTrainee() {
        traineeService.create(trainee);
        verify(traineeDao, times(1)).save(trainee);
    }

    @Test
    void shouldUpdateTraineeIfExists() {
        when(traineeDao.getById(1L)).thenReturn(trainee);

        Trainee updatedTrainee = new Trainee(1L, LocalDate.of(2000, 1, 1), "New Address", user.getId());
        traineeService.update(1L, updatedTrainee);

        verify(traineeDao, times(1)).update(1L, updatedTrainee);
    }

    @Test
    void shouldNotUpdateIfTraineeNotFound() {
        when(traineeDao.getById(1L)).thenReturn(null);

        Trainee updatedTrainee = new Trainee(1L, LocalDate.of(2000, 1, 1), "New Address", user.getId());
        traineeService.update(1L, updatedTrainee);

        verify(traineeDao, never()).update(anyLong(), any(Trainee.class));
    }

    @Test
    void shouldReturnTraineeById() {
        when(traineeDao.getById(1L)).thenReturn(trainee);

        Trainee result = traineeService.getById(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getUserId());
        verify(traineeDao, times(1)).getById(1L);
    }

    @Test
    void shouldReturnNullIfTraineeNotFound() {
        when(traineeDao.getById(1L)).thenReturn(null);

        Trainee result = traineeService.getById(1L);

        assertNull(result);
    }

    @Test
    void shouldReturnAllTrainees() {
        when(traineeDao.getAll()).thenReturn(List.of(trainee));

        List<Trainee> trainees = traineeService.getAll();

        assertFalse(trainees.isEmpty());
        assertEquals(1, trainees.size());
        verify(traineeDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteTraineeIfExists() {
        when(traineeDao.getById(1L)).thenReturn(trainee);

        traineeService.delete(1L);

        verify(traineeDao, times(1)).delete(1L);
    }

    @Test
    void shouldNotDeleteIfTraineeNotFound() {
        when(traineeDao.getById(1L)).thenReturn(null);

        traineeService.delete(1L);

        verify(traineeDao, never()).delete(anyLong());
    }

    @Test
    void shouldReturnFullNameOfTrainee() {
        when(traineeDao.getById(1L)).thenReturn(trainee);
        when(userDao.getById(1L)).thenReturn(user);

        String fullName = traineeService.getTraineeFullName(1L);
        assertEquals(user.getFirstName() + " " + user.getLastName(), fullName);
    }

    @Test
    void shouldThrowExceptionWhenTraineeNotFound() {
        when(traineeDao.getById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> traineeService.getTraineeFullName(1L));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(traineeDao.getById(1L)).thenReturn(trainee);
        when(userDao.getById(1L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> traineeService.getTraineeFullName(1L));
    }
}
