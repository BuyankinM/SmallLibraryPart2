package ru.buyankin.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.buyankin.spring.models.Reader;

import java.util.List;

@Repository
public interface ReadersRepository extends JpaRepository<Reader, Integer> {
    List<Reader> findByName(String name);
}
