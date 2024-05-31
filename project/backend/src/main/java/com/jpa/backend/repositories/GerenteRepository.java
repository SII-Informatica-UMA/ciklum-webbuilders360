package com.jpa.backend.repositories;

import com.jpa.backend.entities.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GerenteRepository extends JpaRepository<Gerente, Long> {
    Gerente findById(long id);

    void deleteById(long id);

    //Añadido para la T3
    Optional<Gerente> findByIdUsuario(Long idUsuario);

    boolean existsByIdUsuario(Long idUsuario);
}
