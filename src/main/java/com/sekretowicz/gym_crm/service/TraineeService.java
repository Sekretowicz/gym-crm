package com.sekretowicz.gym_crm.service;

import com.sekretowicz.gym_crm.dao.TraineeDao;
import com.sekretowicz.gym_crm.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    private TraineeDao dao;

    public void create(Trainee trainee) {
        dao.save(trainee);
        logger.info("Created new trainee: {}", trainee);
    }

    public void update(long id, Trainee trainee) {
        if (dao.getById(id) != null) {
            dao.update(id, trainee);
            logger.info("Updated trainee with ID {}: {}", id, trainee);
        } else {
            logger.warn("Failed to update: trainee with ID {} not found", id);
        }
    }

    public Trainee getById(long id) {
        Trainee trainee = dao.getById(id);
        if (trainee != null) {
            logger.info("Fetched trainee with ID {}: {}", id, trainee);
        } else {
            logger.warn("Trainee with ID {} not found", id);
        }
        return trainee;
    }

    public List<Trainee> getAll() {
        List<Trainee> trainees = dao.getAll();
        logger.info("Fetched all trainees. Total count: {}", trainees.size());
        return trainees;
    }

    public void delete(long id) {
        if (dao.getById(id) != null) {
            dao.delete(id);
            logger.info("Deleted trainee with ID {}", id);
        } else {
            logger.warn("Failed to delete: trainee with ID {} not found", id);
        }
    }
}