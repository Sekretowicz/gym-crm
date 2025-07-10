package com.sekretowicz.gym_crm;

import com.sekretowicz.gym_crm.dto.trainee.TraineeRegistrationRequest;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.User;
import com.sekretowicz.gym_crm.repo.TraineeRepo;
import com.sekretowicz.gym_crm.service.TraineeService;
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
class TraineeServiceTest {
    @Mock
    private TraineeRepo repo;
    @Mock
    private UserService userService;
    @InjectMocks
    private TraineeService service;

    @Test
    void registerValidDataCreatesTrainee() {
        TraineeRegistrationRequest dto = new TraineeRegistrationRequest("John", "Doe", null, null);
        UserCredentials creds = new UserCredentials("u", "p", "t");
        User user = new User();
        when(userService.create(any(User.class))).thenReturn(creds);

        UserCredentials result = service.register(dto);

        assertSame(creds, result);
        verify(repo).save(any(Trainee.class));
    }

    @Test
    void registerWithoutFirstNameThrows() {
        TraineeRegistrationRequest dto = new TraineeRegistrationRequest(null, "Doe", null, null);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.register(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }
}