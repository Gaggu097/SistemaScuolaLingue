package org.gagandeepsuman.dao;

import java.util.List;

public interface IDAO<T, ID> {
    T save(T entity);
    T findById(Class<T> clazz, ID id);
    List<T> findAll(Class<T> clazz);
    T update(T entity);
    void delete(T entity);
    boolean deleteById(Class<T> clazz, ID id);
}