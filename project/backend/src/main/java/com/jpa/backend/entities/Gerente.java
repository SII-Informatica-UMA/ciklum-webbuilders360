package com.jpa.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Gerente {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long idUsuario;
    private String empresa;
}
