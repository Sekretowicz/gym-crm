package com.sekretowicz.gym_crm.repo;

import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepo extends JpaRepository<Trainer, Long> {
    public Trainer findByUser(User user);
}
