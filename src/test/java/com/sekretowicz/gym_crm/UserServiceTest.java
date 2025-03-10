package com.sekretowicz.gym_crm;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sekretowicz.gym_crm.model.User;
import com.sekretowicz.gym_crm.repo.UserRepo;
import com.sekretowicz.gym_crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {
    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setUsername("johndoe");
        testUser.setPassword("password");
        testUser.setActive(true);
    }

    @Test
    void createUser_ShouldGenerateUsernameAndPassword() {
        when(userRepo.save(any(User.class))).thenReturn(testUser);
        userService.create(testUser);
        assertNotNull(testUser.getUsername());
        assertNotNull(testUser.getPassword());
        verify(userRepo, times(1)).save(testUser);
    }
}
