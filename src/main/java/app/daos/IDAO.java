package app.daos;

import java.util.List;

public interface IDAO<T> {
    T findById(Long id);
    List<T> findAll();
    T save(T t);
    T update(T t);
    void delete(Long id);
}