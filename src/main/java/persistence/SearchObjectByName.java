package persistence;

import java.util.Optional;

public interface SearchObjectByName<T> {

    Optional<T> findByName(String name);
}
