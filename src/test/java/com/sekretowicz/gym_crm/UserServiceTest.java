package com.sekretowicz.gym_crm;

import com.sekretowicz.gym_crm.auth.JwtUtil;
import com.sekretowicz.gym_crm.dto_legacy.ChangePasswordDto;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.model.User;
import com.sekretowicz.gym_crm.repo.UserRepo;
import com.sekretowicz.gym_crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private UserService service;

    @Test
    void createGeneratesCredentialsAndSavesUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(jwtUtil.generateToken(anyString())).thenReturn("token");

        UserCredentials creds = service.create(user);

        assertNotNull(creds.getUsername());
        assertNotNull(creds.getPassword());
        assertEquals("token", creds.getToken());
        verify(userRepo).save(user);
        assertEquals("encoded", user.getPassword());
        assertNotNull(user.getUsername());
    }

    @Test
    void changePasswordDtoSuccess() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setUsername("user");
        dto.setOldPassword("old");
        dto.setNewPassword("new");

        User user = new User();
        user.setUsername("user");
        user.setPassword("encOld");

        when(userRepo.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "encOld")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("encNew");

        boolean result = service.changePassword(dto);

        assertTrue(result);
        assertEquals("encNew", user.getPassword());
        verify(userRepo).save(user);
    }

    @Test
    void changePasswordDtoInvalidUsernameThrows() {
        ChangePasswordDto dto = new ChangePasswordDto();
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.changePassword(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void changePasswordDtoWrongOldReturnsFalse() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setUsername("user");
        dto.setOldPassword("old");
        dto.setNewPassword("new");

        User user = new User();
        user.setUsername("user");
        user.setPassword("encOld");

        when(userRepo.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "encOld")).thenReturn(false);

        boolean result = service.changePassword(dto);

        assertFalse(result);
        verify(userRepo, never()).save(any());
    }
}