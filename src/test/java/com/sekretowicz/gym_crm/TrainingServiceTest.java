package com.sekretowicz.gym_crm;

import com.sekretowicz.gym_crm.dto.training.AddTrainingRequest;
import com.sekretowicz.gym_crm.dto.training.VerboseTrainingResponse;
import com.sekretowicz.gym_crm.messaging.dto.WorkloadMessageDto;
import com.sekretowicz.gym_crm.messaging.producer.WorkloadPublisher;
import com.sekretowicz.gym_crm.model.*;
import com.sekretowicz.gym_crm.repo.TrainingRepo;
import com.sekretowicz.gym_crm.service.TraineeService;
import com.sekretowicz.gym_crm.service.TrainerService;
import com.sekretowicz.gym_crm.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {
    @Mock
    private TrainingRepo repo;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private WorkloadPublisher workloadPublisher;
    @InjectMocks
    private TrainingService service;

    @Test
    void addTrainingWithoutTraineeThrows() {
        AddTrainingRequest dto = new AddTrainingRequest(null, "trainer", "name", LocalDate.now(), 30);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.addTraining(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }
}