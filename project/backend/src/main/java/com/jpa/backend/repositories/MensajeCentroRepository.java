package com.jpa.backend.repositories;

import com.jpa.backend.entities.MensajeCentro;

import jakarta.transaction.Transactional;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;

public interface MensajeCentroRepository extends JpaRepository<MensajeCentro, Long> {
   
    MensajeCentro findById(long idMensaje);


    @Query("SELECT m FROM MensajeCentro m WHERE m.centro.id = :centroId")
    List<MensajeCentro> findAllByCentroId(long centroId);

    @Transactional
    @Modifying
    @Query("INSERT INTO MensajeCentro (texto, destinatario) VALUES (:texto, :gerenteId)")
    void createNewMessage(String texto, long gerenteId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MensajeCentro m WHERE m.id = :idMensaje")
    void deleteById(long idMnesaje);

}
