package ru.buyankin.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.buyankin.spring.models.Reader;

import java.util.Optional;

@Repository
public interface ReadersRepository extends JpaRepository<Reader, Integer> {
    Optional<Reader> findByName(String name);
}
