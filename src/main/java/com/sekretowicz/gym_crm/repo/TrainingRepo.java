package com.sekretowicz.gym_crm.repo;

import com.sekretowicz.gym_crm.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepo extends JpaRepository<Training, Long> {
}
