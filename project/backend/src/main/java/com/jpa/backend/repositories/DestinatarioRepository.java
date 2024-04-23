package com.jpa.backend.repositories;

import com.jpa.backend.entities.Destinatario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinatarioRepository extends JpaRepository<Destinatario, Integer> {
}
