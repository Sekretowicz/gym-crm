package com.sekretowicz.gym_crm.dao;
import com.sekretowicz.gym_crm.model.Trainee;
import java.util.List;

public class TraineeDao extends AbstractDao<Trainee> {

    @Override
    public List<Trainee> getAll() {
        return List.of();
    }

    @Override
    public Trainee getById(Long id) {
        return null;
    }

    @Override
    public void save(Trainee entity) {
    }

    @Override
    public void delete(Long id) {
    }
}