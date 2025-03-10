package com.sekretowicz.gym_crm.repo;

import com.sekretowicz.gym_crm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
