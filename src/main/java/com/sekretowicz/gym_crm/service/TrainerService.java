package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.dao.TrainerDao;
import com.sekretowicz.gym_crm.dao.UserDao;
import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    private TrainerDao trainerDao;
    @Autowired
    private UserDao userDao;

    public void create(Trainer trainer) {
        trainerDao.save(trainer);
        logger.info("Created new trainer: {}", trainer);
    }

    public void update(long id, Trainer trainer) {
        if (trainerDao.getById(id) != null) {
            trainerDao.update(id, trainer);
            logger.info("Updated trainer with ID {}: {}", id, trainer);
        } else {
            logger.warn("Failed to update: trainer with ID {} not found", id);
        }
    }

    public Trainer getById(long id) {
        Trainer trainer = trainerDao.getById(id);
        if (trainer != null) {
            logger.info("Fetched trainer with ID {}: {}", id, trainer);
        } else {
            logger.warn("Trainer with ID {} not found", id);
        }
        return trainer;
    }

    public List<Trainer> getAll() {
        List<Trainer> trainers = trainerDao.getAll();
        logger.info("Fetched all trainers. Total count: {}", trainers.size());
        return trainers;
    }

    public String getTrainerFullName(Long trainerId) {
        Trainer trainer = trainerDao.getById(trainerId);
        if (trainer == null) {
            throw new NoSuchElementException("Trainer with ID " + trainerId + " not found.");
        }

        User user = userDao.getById(trainer.getUserId());
        if (user == null) {
            throw new NoSuchElementException("User with ID " + trainer.getUserId() + " not found.");
        }

        return user.getFirstName() + " " + user.getLastName();
    }
}