package com.sekretowicz.gym_crm.dao;
import com.sekretowicz.gym_crm.model.Training;

import java.util.List;

public class TrainingDao extends AbstractDao<Training> {
    @Override
    public List<Training> getAll() {
        return List.of();
    }

    @Override
    public Training getById(Long id) {
        return null;
    }

    @Override
    public void save(Training entity) {
    }

    @Override
    public void delete(Long id) {
    }
}
