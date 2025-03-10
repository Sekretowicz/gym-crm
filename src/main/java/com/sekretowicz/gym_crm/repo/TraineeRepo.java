package com.sekretowicz.gym_crm.repo;

import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepo extends JpaRepository<Trainee, Long> {
    public Trainee findByUser(User user);
    public void deleteByUser(User user);
}
