package org.mabartos.service.core;

import java.util.Set;

public interface CRUDService<T> {

    T create(T entity);

    T updateByID(Long id, T entity);

    Set<T> getAll();

    T findByID(Long id);

    boolean exists(Long id);

    boolean deleteByID(Long id);
}
