package persistence;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T> {

    T save(T obj);

    List<T> findAll();

    void update(T obj);

    void delete(T obj);
}

