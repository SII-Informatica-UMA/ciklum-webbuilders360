package com.jpa.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Destinatario {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
}
