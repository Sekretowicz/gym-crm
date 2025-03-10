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
public class TraineeService {
    @Autowired
    private TraineeRepo repo;
    @Autowired
    private UserService userService;
    @Autowired
    private TrainerRepo trainerRepo;
    @Autowired
    private EntityManager entityManager;

    public void create(Trainee trainee) {
        log.info("Creating trainee: {}", trainee);
        repo.save(trainee);
    }

    public Trainee getByUsername(String username) {
        log.info("Fetching trainee by username: {}", username);
        User user = userService.getByUsername(username);
        return repo.findByUser(user);
    }

    public void update(Trainee trainee) {
        log.info("Updating trainee: {}", trainee);
        if (repo.existsById(trainee.getId())) {
            repo.save(trainee);
        } else {
            throw new RuntimeException("Trainee not found");
        }
    }

    public void changePassword(long id, String password) {
        log.info("Changing password for trainee ID: {}", id);
        Trainee trainee = repo.findById(id).orElseThrow(() -> new RuntimeException("Trainee not found"));
        userService.changePassword(trainee.getUser().getId(), password);
    }

    public void setActive(long id, boolean isActive) {
        log.info("Setting active status to {} for trainee ID: {}", isActive, id);
        Trainee trainee = repo.findById(id).orElseThrow(() -> new RuntimeException("Trainee not found"));
        userService.setActive(trainee.getUser().getId(), isActive);
    }

    public void deleteByUsername(String username) {
        log.info("Deleting trainee by username: {}", username);
        User user = userService.getByUsername(username);
        repo.deleteByUser(user);
    }

    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        log.info("Fetching trainings for trainee: {} with filters", traineeUsername);
        Trainee trainee = getByUsername(traineeUsername);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> trainingRoot = query.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(trainingRoot.get("trainee"), trainee));
        if (fromDate != null) predicates.add(cb.greaterThanOrEqualTo(trainingRoot.get("trainingDate"), fromDate));
        if (toDate != null) predicates.add(cb.lessThanOrEqualTo(trainingRoot.get("trainingDate"), toDate));
        if (trainerName != null && !trainerName.isEmpty()) {
            Join<Training, Trainer> trainerJoin = trainingRoot.join("trainer");
            predicates.add(cb.equal(trainerJoin.get("user").get("username"), trainerName));
        }
        if (trainingType != null && !trainingType.isEmpty()) {
            predicates.add(cb.equal(trainingRoot.get("trainingType"), trainingType));
        }
        query.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for trainee: {}", traineeUsername);
        Trainee trainee = getByUsername(traineeUsername);
        List<Trainer> assignedTrainers = trainee.getTrainers();
        List<Trainer> allTrainers = trainerRepo.findAll();
        allTrainers.removeAll(assignedTrainers);
        return allTrainers;
    }

    public void updateTraineeTrainers(String traineeUsername, List<Long> trainerIds) {
        log.info("Updating trainer list for trainee: {}", traineeUsername);
        Trainee trainee = getByUsername(traineeUsername);
        List<Trainer> trainers = trainerRepo.findAllById(trainerIds);
        trainee.setTrainers(trainers);
        repo.save(trainee);
    }
}