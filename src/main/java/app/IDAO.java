package app;

import java.util.List;

public interface IDAO<T> {
    T findById(Integer id);
    List<T> findAll();
    T save(T t);
    T update(T t);
    void delete(Integer id);
}