package com.jpa.backend.repositories;

import com.jpa.backend.entities.MensajeCentro;

import jakarta.transaction.Transactional;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;

public interface MensajeCentroRepository extends JpaRepository<MensajeCentro, Long> {
   
    //Obtiene un mensaje de centro concreto
    MensajeCentro findById(long idMensaje);

    //Permite consultar todos los mensajes de un centro.
    @Query("SELECT m FROM MensajeCentro m WHERE m.centro = :centroId")
    List<MensajeCentro> findAllByCentroId(long centroId);
    
}
