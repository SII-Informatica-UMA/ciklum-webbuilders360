package com.jpa.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Destinatario {
    @Id
    @Column(name = "id_destinatario") //TODO revisar nombre columna Y es clave primaria?
    private Long id;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
}
