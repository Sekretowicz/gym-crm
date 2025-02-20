package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.dao.TrainerDao;
import com.sekretowicz.gym_crm.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    private TrainerDao dao;

    public void create(Trainer trainer) {
        dao.save(trainer);
        logger.info("Created new trainer: {}", trainer);
    }

    public void update(long id, Trainer trainer) {
        if (dao.getById(id) != null) {
            dao.update(id, trainer);
            logger.info("Updated trainer with ID {}: {}", id, trainer);
        } else {
            logger.warn("Failed to update: trainer with ID {} not found", id);
        }
    }

    public Trainer getById(long id) {
        Trainer trainer = dao.getById(id);
        if (trainer != null) {
            logger.info("Fetched trainer with ID {}: {}", id, trainer);
        } else {
            logger.warn("Trainer with ID {} not found", id);
        }
        return trainer;
    }

    public List<Trainer> getAll() {
        List<Trainer> trainers = dao.getAll();
        logger.info("Fetched all trainers. Total count: {}", trainers.size());
        return trainers;
    }
}