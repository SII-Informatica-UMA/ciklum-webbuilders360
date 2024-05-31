package com.jpa.backend.repositories;

import com.jpa.backend.entities.MensajeCentro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MensajeCentroRepository extends JpaRepository<MensajeCentro, Long> {
    MensajeCentro findById(long idMensaje);

    @Query("SELECT m FROM MensajeCentro m WHERE m.centro = :centroId")
    List<MensajeCentro> findAllByCentroId(long centroId);

}
