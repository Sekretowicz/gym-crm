package com.sekretowicz.gym_crm;

import com.sekretowicz.gym_crm.dto.trainer.TrainerRegistrationRequest;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.TrainingType;
import com.sekretowicz.gym_crm.model.User;
import com.sekretowicz.gym_crm.repo.TrainerRepo;
import com.sekretowicz.gym_crm.service.TrainerService;
import com.sekretowicz.gym_crm.service.TrainingTypeService;
import com.sekretowicz.gym_crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @Mock
    private TrainerRepo repo;
    @Mock
    private UserService userService;
    @Mock
    private TrainingTypeService trainingTypeService;
    @InjectMocks
    private TrainerService service;

    @Test
    void registerValidDataCreatesTrainer() {
        TrainerRegistrationRequest dto = new TrainerRegistrationRequest("John", "Doe", 1L);
        UserCredentials creds = new UserCredentials("u", "p", "t");
        User user = new User();
        when(userService.create(any(User.class))).thenReturn(creds);
        when(trainingTypeService.getById(1L)).thenReturn(new TrainingType("spec"));

        UserCredentials result = service.register(dto);

        assertSame(creds, result);
        verify(repo).save(any(Trainer.class));
    }

    @Test
    void registerWithoutFirstNameThrows() {
        TrainerRegistrationRequest dto = new TrainerRegistrationRequest(null, "Doe", 1L);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.register(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

}