package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.dto.ChangePasswordDto;
import com.sekretowicz.gym_crm.model.User;
import com.sekretowicz.gym_crm.repo.UserRepo;
import com.sekretowicz.gym_crm.utils.UserUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepo repo;

    public void create(User user) {
        log.info("Generating username and password for: {} {}", user.getFirstName(), user.getLastName());
        user.setUsername(UserUtils.generateUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(UserUtils.generatePassword(8));

        repo.save(user);
        log.info("User created with username: {}", user.getUsername());
    }

    public User getByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return repo.findByUsername(username);
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

    //REST task     17.03.2025

    public boolean login(String username, String password) {
        User user = getByUsername(username);

        //TODO: Что если выбрасывать тут исключения, а обрабатывать в контроллере?
        if (!user.getPassword().equals(password)) {
            return false;
        }

        return true;
    }

    //Создадим перегрузку changePassword
    @Transactional
    public boolean changePassword(ChangePasswordDto dto) {
        User user = getByUsername(dto.getUsername());
        //TODO: Добавить исключение при неправильном пароле
        if (!user.getPassword().equals(dto.getOldPassword())) {
            return false;
        }
        user.setPassword(dto.getNewPassword());
        repo.save(user);
        return true;
    }
}
