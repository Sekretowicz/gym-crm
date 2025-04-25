package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.auth.JwtUtil;
import com.sekretowicz.gym_crm.auth.LoginAttempt;
import com.sekretowicz.gym_crm.dto_legacy.ChangePasswordDto;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.model.User;
import com.sekretowicz.gym_crm.repo.UserRepo;
import com.sekretowicz.gym_crm.utils.UserUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepo repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;


    //UPD 20.04: Now it would return UserCredentials
    public UserCredentials create(User user) {
        log.info("Generating username and password for: {} {}", user.getFirstName(), user.getLastName());
        String username = UserUtils.generateUsername(user.getFirstName(), user.getLastName());
        String password = UserUtils.generatePassword(8);
        String token = jwtUtil.generateToken(username);
        UserCredentials uc = new UserCredentials(username, password, token);

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        repo.save(user);
        log.info("User created with username: {}", username);
        return uc;
    }

    public User getByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return repo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public void update(User user) {
        log.info("Updating user: {}", user);
        if (repo.existsById(user.getId())) {
            repo.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    public void changePassword(long id, String password) {
        log.info("Changing password for user ID: {}", id);
        User user = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(password);
        repo.save(user);
    }

    public void setActive(long id, boolean isActive) {
        log.info("Setting active status to {} for user ID: {}", isActive, id);
        User user = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(isActive);
        repo.save(user);
    }

    @Transactional
    public boolean changePassword(ChangePasswordDto dto) throws ResponseStatusException {
        //Validation
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (dto.getOldPassword() == null || dto.getOldPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is required");
        }
        if (dto.getNewPassword() == null || dto.getNewPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password is required");
        }

        User user = getByUsername(dto.getUsername());
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        repo.save(user);
        return true;
    }
}
