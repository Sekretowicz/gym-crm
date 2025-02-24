package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.dao.TraineeDao;
import com.sekretowicz.gym_crm.dao.UserDao;
import com.sekretowicz.gym_crm.model.Trainee;
import com.sekretowicz.gym_crm.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    private TraineeDao traineeDao;
    @Autowired
    private UserDao userDao;

    public void create(Trainee trainee) {
        traineeDao.save(trainee);
        logger.info("Created new trainee: {}", trainee);
    }

    public void update(long id, Trainee trainee) {
        if (traineeDao.getById(id) != null) {
            traineeDao.update(id, trainee);
            logger.info("Updated trainee with ID {}: {}", id, trainee);
        } else {
            logger.warn("Failed to update: trainee with ID {} not found", id);
        }
    }

    public Trainee getById(long id) {
        Trainee trainee = traineeDao.getById(id);
        if (trainee != null) {
            logger.info("Fetched trainee with ID {}: {}", id, trainee);
        } else {
            logger.warn("Trainee with ID {} not found", id);
        }
        return trainee;
    }

    public List<Trainee> getAll() {
        List<Trainee> trainees = traineeDao.getAll();
        logger.info("Fetched all trainees. Total count: {}", trainees.size());
        return trainees;
    }

    public void delete(long id) {
        if (traineeDao.getById(id) != null) {
            traineeDao.delete(id);
            logger.info("Deleted trainee with ID {}", id);
        } else {
            logger.warn("Failed to delete: trainee with ID {} not found", id);
        }
    }

    public String getTraineeFullName(Long traineeId) {
        Trainee trainee = traineeDao.getById(traineeId);
        if (trainee == null) {
            throw new NoSuchElementException("Trainee with ID " + traineeId + " not found.");
        }

        User user = userDao.getById(trainee.getUserId());
        if (user == null) {
            throw new NoSuchElementException("User with ID " + trainee.getUserId() + " not found.");
        }

        return user.getFirstName() + " " + user.getLastName();
    }
}