package com.sekretowicz.gym_crm.dao;
import com.sekretowicz.gym_crm.model.Trainer;

import java.util.List;

public class TrainerDao extends AbstractDao<Trainer> {
    @Override
    public List<Trainer> getAll() {
        return List.of();
    }

    @Override
    public Trainer getById(Long id) {
        return null;
    }

    @Override
    public void save(Trainer entity) {
    }

    @Override
    public void delete(Long id) {
    }
}
