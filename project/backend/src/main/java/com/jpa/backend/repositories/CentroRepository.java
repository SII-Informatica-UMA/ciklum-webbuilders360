package com.jpa.backend.repositories;

import com.jpa.backend.entities.Centro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentroRepository extends JpaRepository<Centro, Long> {
    Centro findById(long id);
    void deleteById(long id);
}
