package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.dto.common.SetActiveRequest;
import com.sekretowicz.gym_crm.dto.trainee.TraineeProfileResponse;
import com.sekretowicz.gym_crm.dto.trainee.TraineeRegistrationRequest;
import com.sekretowicz.gym_crm.dto.trainee.TraineeUpdateRequest;
import com.sekretowicz.gym_crm.dto.trainee.UpdateTraineeTrainersRequest;
import com.sekretowicz.gym_crm.dto.trainer.TrainerShortDto;
import com.sekretowicz.gym_crm.dto.training.TrainingResponse;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.metrics.CustomMetrics;
import com.sekretowicz.gym_crm.model.*;
import com.sekretowicz.gym_crm.repo.*;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.EntityManager;

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
    @Autowired
    private TrainerService trainerService;

    //Prometheus metrics
    @Autowired
    private CustomMetrics customMetrics;

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

    @Transactional
    public void deleteByUsername(String username) {
        log.info("Deleting trainee by username: {}", username);
        User user = userService.getByUsername(username);
        repo.deleteByUser(user);
    }

    /*
    //Old version

    public List<TrainingResponse> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
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
        List<Training> resultList = entityManager.createQuery(query).getResultList();
        return resultList.stream().map(TrainingResponse::new).toList();
    }
     */

    public List<TrainingResponse> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        log.info("Fetching trainings for trainee: {} with filters", traineeUsername);
        Trainee trainee = getByUsername(traineeUsername);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> trainingRoot = query.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(trainingRoot.get("trainee"), trainee));

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(trainingRoot.get("trainingDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(trainingRoot.get("trainingDate"), toDate));
        }

        if (trainerName != null && !trainerName.isEmpty()) {
            Join<Training, Trainer> trainerJoin = trainingRoot.join("trainer");
            predicates.add(cb.equal(trainerJoin.get("user").get("username"), trainerName));
        }

        if (trainingType != null && !trainingType.isEmpty()) {
            Join<Training, TrainingType> typeJoin = trainingRoot.join("trainingType");
            predicates.add(cb.equal(typeJoin.get("trainingTypeName"), trainingType));
        }

        query.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
        List<Training> resultList = entityManager.createQuery(query).getResultList();
        return resultList.stream().map(TrainingResponse::new).toList();
    }

    public List<TrainerShortDto> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for trainee: {}", traineeUsername);
        Trainee trainee = getByUsername(traineeUsername);
        List<Trainer> assignedTrainers = trainee.getTrainers();
        List<Trainer> allTrainers = trainerRepo.findAll();
        allTrainers.removeAll(assignedTrainers);

        return allTrainers.stream()
                .map(TrainerShortDto::new)
                .toList();
    }

    public void updateTraineeTrainers(String traineeUsername, List<Long> trainerIds) {
        log.info("Updating trainer list for trainee: {}", traineeUsername);
        Trainee trainee = getByUsername(traineeUsername);
        List<Trainer> trainers = trainerRepo.findAllById(trainerIds);
        trainee.setTrainers(trainers);
        repo.save(trainee);
    }

    //REST task     17.03.2025
    @Transactional
    public UserCredentials register(TraineeRegistrationRequest dto) throws ResponseStatusException {
        //Validation
        if (dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
            log.warn("Registration failed: First name is missing");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().isEmpty()) {
            log.warn("Registration failed: Last name is missing");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
        }

        //Creating a user
        log.info("Registering new trainee with firstName: {}, lastName: {}", dto.getFirstName(), dto.getLastName());
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setActive(true);
        userService.create(user);
        log.info("User created with username: {}", user.getUsername());

        //Creating a Trainee
        log.debug("Creating Trainee entity");
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(dto.getDateOfBirth());
        trainee.setAddress(dto.getAddress());
        create(trainee);
        log.info("Trainee created for user: {}", user.getUsername());

        UserCredentials response = new UserCredentials();
        response.setUsername(user.getUsername());
        response.setPassword(user.getPassword());
        log.info("Trainee registration completed successfully for username: {}", response.getUsername());

        //Incrementing the registration metric
        customMetrics.incrementTraineeRegistration();

        return response;
    }

    //5. Get Trainee Profile (GET method)
    public TraineeProfileResponse getTraineeProfile(String username) {
        //Validation: Username required
        if (username == null || username.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        log.info("Fetching profile for trainee with username: {}", username);
        Trainee trainee = getByUsername(username);
        return new TraineeProfileResponse(trainee);
    }

    //6. Update Trainee Profile (PUT method)
    @Transactional
    public TraineeProfileResponse updateTraineeProfile (TraineeUpdateRequest dto) throws ResponseStatusException {
        //Validation: username, firstName, lastName required
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
        }
        //Update user
        User user = userService.getByUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setActive(dto.isActive());
        userService.update(user);

        //Update trainee
        Trainee trainee = repo.findByUser(user);
        trainee.setDateOfBirth(dto.getDateOfBirth());
        trainee.setAddress(dto.getAddress());
        update(trainee);

        return new TraineeProfileResponse(trainee);
    }

    //11. Update Trainee's Trainer List (PUT method)
    @Transactional
    public List<TrainerShortDto> updateTrainerList(String username, UpdateTraineeTrainersRequest request) {
        Trainee trainee = getByUsername(username);
        List<Trainer> newTrainersList = request.getTrainers().stream()
                .map(trainerService::getByUsername)
                .toList();
        trainee.getTrainers().addAll(newTrainersList);
        update(trainee);

        return trainee.getTrainers().stream()
                .map(TrainerShortDto::new)
                .toList();
    }

    //15. Activate/De-Activate Trainee (PATCH method)
    public void setActive(SetActiveRequest request) {
        Trainee trainee = getByUsername(request.getUsername());
        User user = trainee.getUser();
        user.setActive(request.isActive());
        userService.update(user);
    }

}