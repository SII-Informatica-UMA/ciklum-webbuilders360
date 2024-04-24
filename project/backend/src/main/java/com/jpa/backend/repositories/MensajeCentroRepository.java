package com.jpa.backend.repositories;

import com.jpa.backend.entities.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajeCentroRepository extends JpaRepository<Mensaje, Long> {
}
