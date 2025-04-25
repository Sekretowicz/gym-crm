package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.dto.common.SetActiveRequest;
import com.sekretowicz.gym_crm.dto.trainer.TrainerProfileResponse;
import com.sekretowicz.gym_crm.dto.trainer.TrainerRegistrationRequest;
import com.sekretowicz.gym_crm.dto.trainer.TrainerUpdateRequest;
import com.sekretowicz.gym_crm.dto.training.TrainingResponse;
import com.sekretowicz.gym_crm.dto_legacy.UserCredentials;
import com.sekretowicz.gym_crm.model.*;
import com.sekretowicz.gym_crm.repo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

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
    @Autowired
    private TrainingTypeService trainingTypeService;

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

    public List<TrainingResponse> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
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
        List<Training> resultList = entityManager.createQuery(query).getResultList();
        return resultList.stream().map(TrainingResponse::new).toList();
    }

    //REST task     17.03.2025
    @Transactional  
    public UserCredentials register(TrainerRegistrationRequest dto) {
        //Validation
        if (dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
        }
        if (dto.getSpecId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specialization is required");
        }

        //Creating a user
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setActive(true);
        UserCredentials response = userService.create(user);

        //Creating a Trainer
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainingTypeService.getById(dto.getSpecId()));
        create(trainer);

        return response;
    }

    //8. Get Trainer Profile (GET method)
    public TrainerProfileResponse getTrainerProfile(String username) {
        return new TrainerProfileResponse(getByUsername(username));
    }

    //9. Update Trainer Profile (PUT method)
    @Transactional
    public TrainerProfileResponse updateTrainerProfile(TrainerUpdateRequest dto) {
        //Validation: username, firstName, lastName, specialization, isActive required
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
        }
        //FIXME: Figure out how to handle specialization
        /*
        if (dto.getSpecialization() == null || dto.getSpecialization().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specialization is required");
        }
         */
        //Update user
        User user = userService.getByUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setActive(dto.isActive());
        userService.update(user);

        //Update trainer
        Trainer trainer = repo.findByUser(user);
        //trainer.setSpecialization(dto.getSpecialization());
        update(trainer);

        return new TrainerProfileResponse(trainer);
    }

    //16. Activate/De-Activate Trainer (PATCH method)
    @Transactional
    public void setActive(SetActiveRequest request) {
        Trainer trainer = getByUsername(request.getUsername());
        User user = trainer.getUser();
        user.setActive(request.isActive());
        userService.update(user);
    }
}