package com.jpa.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Destinatario {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
}
