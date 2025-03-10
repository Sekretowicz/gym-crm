package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.model.*;
import com.sekretowicz.gym_crm.repo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TrainerService {
    @Autowired
    private TrainerRepo repo;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityManager entityManager;

    public void create(Trainer trainer) {
        log.info("Creating trainer: {}", trainer);
        repo.save(trainer);
    }

    public Trainer getByUsername(String username) {
        log.info("Fetching trainer by username: {}", username);
        User user = userService.getByUsername(username);
        return repo.findByUser(user);
    }

    public void update(Trainer trainer) {
        log.info("Updating trainer: {}", trainer);
        if (repo.existsById(trainer.getId())) {
            repo.save(trainer);
        } else {
            throw new RuntimeException("Trainer not found");
        }
    }

    public void changePassword(long id, String password) {
        log.info("Changing password for trainer ID: {}", id);
        Trainer trainer = repo.findById(id).orElseThrow(() -> new RuntimeException("Trainer not found"));
        userService.changePassword(trainer.getUser().getId(), password);
    }

    public void setActive(long id, boolean isActive) {
        log.info("Setting active status to {} for trainer ID: {}", isActive, id);
        Trainer trainer = repo.findById(id).orElseThrow(() -> new RuntimeException("Trainer not found"));
        userService.setActive(trainer.getUser().getId(), isActive);
    }

    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        log.info("Fetching trainings for trainer: {} with filters", trainerUsername);
        Trainer trainer = getByUsername(trainerUsername);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> trainingRoot = query.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(trainingRoot.get("trainer"), trainer));
        if (fromDate != null) predicates.add(cb.greaterThanOrEqualTo(trainingRoot.get("trainingDate"), fromDate));
        if (toDate != null) predicates.add(cb.lessThanOrEqualTo(trainingRoot.get("trainingDate"), toDate));
        if (traineeName != null && !traineeName.isEmpty()) {
            Join<Training, Trainee> traineeJoin = trainingRoot.join("trainee");
            predicates.add(cb.equal(traineeJoin.get("user").get("username"), traineeName));
        }
        query.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }
}