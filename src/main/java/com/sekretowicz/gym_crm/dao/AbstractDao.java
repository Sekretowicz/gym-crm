package com.sekretowicz.gym_crm.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<T> {
    private Map<Long, T> data;

    public List<T> getAll() {
        return new ArrayList<>(data.values());
    };
    public T getById(Long id) {
        return data.get(id);
    };
    public void save(T entity) {

    };
    abstract void delete(Long id);
}

