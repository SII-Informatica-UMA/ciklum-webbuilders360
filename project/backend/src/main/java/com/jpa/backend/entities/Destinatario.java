package com.jpa.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class Destinatario {
    @Column(name = "id_destinatario")
    private Long id;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
}
