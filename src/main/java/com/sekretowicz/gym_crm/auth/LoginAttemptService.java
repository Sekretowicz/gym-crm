package com.sekretowicz.gym_crm.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class LoginAttemptService {
    private final Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_MINUTES = 1;

    public boolean isLocked(String username) {
        LoginAttempt attempt = attempts.get(username);
        return attempt != null && attempt.getLockedUntil() != null
                && attempt.getLockedUntil().isAfter(LocalDateTime.now());
    }

    public void registerFailure(String username) {
        LoginAttempt attempt = attempts.getOrDefault(username, new LoginAttempt());
        attempt.incrementAttempts();
        log.info("Login attempt for user {}: {} attempts", username, attempt.getAttempts());
        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            attempt.lockForMinutes(LOCK_MINUTES);
            log.warn("User {} is locked for {} minutes", username, LOCK_MINUTES);
        }
        attempts.put(username, attempt);
    }

    public void reset(String username) {
        attempts.remove(username);
    }

}
