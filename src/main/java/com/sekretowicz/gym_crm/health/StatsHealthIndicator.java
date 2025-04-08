package com.sekretowicz.gym_crm.health;

import com.sekretowicz.gym_crm.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class StatsHealthIndicator implements HealthIndicator {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserRepo traineeRepo;
    @Autowired
    private UserRepo trainerRepo;
    @Autowired
    private UserRepo trainingRepo;

    @Override
    public Health health() {
        try {
            long userCount = userRepo.count();
            long traineeCount = traineeRepo.count();
            long trainerCount = trainerRepo.count();
            long trainingCount = trainingRepo.count();

            return Health.up()
                    .withDetail("total_users", userCount)
                    .withDetail("trainees", traineeCount)
                    .withDetail("trainers", trainerCount)
                    .withDetail("trainings", trainingCount)
                    .build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
