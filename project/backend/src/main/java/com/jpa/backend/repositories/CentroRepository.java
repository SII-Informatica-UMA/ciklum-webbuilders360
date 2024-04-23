package com.jpa.backend.repositories;

import com.jpa.backend.entities.Centro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CentroRepository extends JpaRepository<Centro, Long> {

}
