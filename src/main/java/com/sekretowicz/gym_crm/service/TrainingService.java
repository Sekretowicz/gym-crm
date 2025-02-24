package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.dao.TrainingDao;
import com.sekretowicz.gym_crm.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    @Autowired
    private TrainingDao dao;

    public void create(Training training) {
        dao.save(training);
        logger.info("Created new training: {}", training);
    }

    public Training getById(long id) {
        Training training = dao.getById(id);
        if (training != null) {
            logger.info("Fetched training with ID {}: {}", id, training);
        } else {
            logger.warn("Training with ID {} not found", id);
        }
        return training;
    }

    public List<Training> getAll() {
        List<Training> trainings = dao.getAll();
        logger.info("Fetched all trainings. Total count: {}", trainings.size());
        return trainings;
    }
}