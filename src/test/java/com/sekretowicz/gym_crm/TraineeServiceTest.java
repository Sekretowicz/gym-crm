package com.sekretowicz.gym_crm;

import static org.mockito.Mockito.*;

import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.User;
import com.sekretowicz.gym_crm.repo.TraineeRepo;
import com.sekretowicz.gym_crm.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;

class TraineeServiceTest {
    @Mock
    private TraineeRepo traineeRepo;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee testTrainee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testTrainee = new Trainee();
        testTrainee.setId(1L);
        testTrainee.setUser(new User());
        testTrainee.setDateOfBirth(LocalDate.of(1995, 5, 20));
        testTrainee.setAddress("123 Main St");
    }

    @Test
    void createTrainee_ShouldSaveTrainee() {
        when(traineeRepo.save(any(Trainee.class))).thenReturn(testTrainee);
        traineeService.create(testTrainee);
        verify(traineeRepo, times(1)).save(testTrainee);
    }
}
