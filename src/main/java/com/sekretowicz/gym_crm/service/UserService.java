package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.dao.UserDao;
import com.sekretowicz.gym_crm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UserService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private UserDao userDao;

    public void create(User user) {
        generatePassword(user);
        generateUsername(user);

    }

    private void generateUsername(User user) {
        String baseUsername = user.getFirstName() + "." + user.getLastName();
        String username = baseUsername;
        int counter = 1;

        while (userDao.getByUsername(username) != null) {
            username = baseUsername + counter;
            counter++;
        }

        user    .setUsername(username);
    }

    private void generatePassword(User user) {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        user.setPassword(password.toString());
    }
}