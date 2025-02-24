package com.sekretowicz.gym_crm.dao;

import com.sekretowicz.gym_crm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends AbstractDao<User> {
    @Autowired
    private UserDao userDao;

    public User getByUsername(String username) {
        return data.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
