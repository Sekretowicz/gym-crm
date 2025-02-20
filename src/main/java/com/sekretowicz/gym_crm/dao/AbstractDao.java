package com.sekretowicz.gym_crm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public abstract class AbstractDao<T> {
    protected Map<Long, T> data = new HashMap<>();
    private static long idCounter = 0;
    private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

    public List<T> getAll() {
        logger.info("Fetching all entities. Total count: {}", data.size());
        return new ArrayList<>(data.values());
    }

    public T getById(Long id) {
        T entity = data.get(id);
        if (entity != null) {
            logger.info("Entity found with ID {}", id);
        } else {
            logger.warn("Entity with ID {} not found", id);
        }
        return entity;
    }

    public void save(T entity) {
        long newId = ++idCounter;
        data.put(newId, entity);
        logger.info("Saved new entity with ID {}: {}", newId, entity);
    }

    public void update(long id, T entity) {
        if (data.containsKey(id)) {
            data.put(id, entity);
            logger.info("Updated entity with ID {}: {}", id, entity);
        } else {
            logger.warn("Failed to update: entity with ID {} not found", id);
        }
    }

    public void delete(Long id) {
        if (data.remove(id) != null) {
            logger.info("Deleted entity with ID {}", id);
        } else {
            logger.warn("Failed to delete: entity with ID {} not found", id);
        }
    }
}