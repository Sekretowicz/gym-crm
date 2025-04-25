package com.sekretowicz.gym_crm.auth;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginAttempt {
    private int attempts = 0;
    private LocalDateTime lockedUntil;

    public void incrementAttempts() {
        attempts++;
    }
    public void lockForMinutes(int minutes) {
        lockedUntil = LocalDateTime.now().plusMinutes(minutes);
        attempts = 0;
    }
}