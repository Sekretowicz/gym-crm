package com.sekretowicz.gym_crm.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter traineeRegistrations;
    private final Counter trainerRegistrations;
    private final Counter loginSuccess;
    private final Counter loginFailure;

    public CustomMetrics(MeterRegistry registry) {
        this.traineeRegistrations = Counter.builder("registrations_trainees_total")
                .description("Total number of trainee registrations")
                .register(registry);

        this.trainerRegistrations = Counter.builder("registrations_trainers_total")
                .description("Total number of trainer registrations")
                .register(registry);

        this.loginSuccess = Counter.builder("login_success_total")
                .description("Successful login attempts")
                .register(registry);

        this.loginFailure = Counter.builder("login_failure_total")
                .description("Failed login attempts")
                .register(registry);
    }

    public void incrementTraineeRegistration() {
        traineeRegistrations.increment();
    }

    public void incrementTrainerRegistration() {
        trainerRegistrations.increment();
    }

    public void incrementLoginSuccess() {
        loginSuccess.increment();
    }

    public void incrementLoginFailure() {
        loginFailure.increment();
    }
}