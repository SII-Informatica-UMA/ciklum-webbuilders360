package com.jpa.backend.repositories;

import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GerenteRepository extends JpaRepository<Gerente, Long> {
    Gerente findById(long id);
    @Query("select c from Centro c where c.gerenteAsociado = :id")
    Centro centroAsociado(@Param("id") Long id);

    void deleteById(long id);
    //añadido para la T3
    Optional<Gerente> findByIdUsuario(Long idUsuario);
    boolean existsByIdUsuario(Long idUsuario);
}
