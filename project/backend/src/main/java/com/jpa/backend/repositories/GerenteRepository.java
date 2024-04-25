package com.jpa.backend.repositories;

import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GerenteRepository extends JpaRepository<Gerente, Long> {
    Gerente findById(long id);
    @Query("select c from Centro where c.id_gerente = :id")
    List<Centro> centrosAsociados(@Param("id") Long id);

    void deleteById(long id);
}
