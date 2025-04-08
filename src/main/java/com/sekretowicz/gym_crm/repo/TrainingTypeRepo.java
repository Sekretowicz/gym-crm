package com.sekretowicz.gym_crm.repo;

import com.sekretowicz.gym_crm.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepo extends JpaRepository<TrainingType, Long> {
    public TrainingType findByTrainingTypeName(String trainingTypeName);
}
