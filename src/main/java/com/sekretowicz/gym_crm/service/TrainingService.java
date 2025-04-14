package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.dto.training.AddTrainingRequest;
import com.sekretowicz.gym_crm.model.*;
import com.sekretowicz.gym_crm.repo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TrainingService {
    @Autowired
    private TrainingRepo repo;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Training addTraining(Training training) {
        log.info("Adding training: {}", training);
        return repo.save(training);
    }

    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        log.info("Fetching trainings for trainee: {}", traineeUsername);
        Trainee trainee = traineeService.getByUsername(traineeUsername);
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

    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        log.info("Fetching trainings for trainer: {}", trainerUsername);
        Trainer trainer = trainerService.getByUsername(trainerUsername);
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

    //REST task

    //14. Add Training (POST method)
    @Transactional
    public void addTraining(AddTrainingRequest dto) throws ResponseStatusException {
        //Validation: everything required except trainingType
        if (dto.getTraineeUsername() == null || dto.getTraineeUsername().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trainee name is required");
        }
        if (dto.getTrainerUsername() == null || dto.getTrainerUsername().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trainer name is required");
        }
        if (dto.getTrainingName() == null || dto.getTrainingName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Training name is required");
        }
        if (dto.getTrainingDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Training date is required");
        }
        if (dto.getTrainingDuration() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Training duration must be greater than 0");
        }

        Training training = new Training();
        Trainee trainee = traineeService.getByUsername(dto.getTraineeUsername());
        Trainer trainer = trainerService.getByUsername(dto.getTrainerUsername());
        training.setTrainingType(trainer.getSpecialization());

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(dto.getTrainingName());
        training.setTrainingDate(dto.getTrainingDate());
        training.setTrainingDuration(dto.getTrainingDuration());

        repo.save(training);
    }
}
