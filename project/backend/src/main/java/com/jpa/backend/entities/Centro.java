package com.jpa.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Centro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "centro_seq")
    @SequenceGenerator(name = "centro_seq", sequenceName = "centro_seq")
    private Long id;
    private String nombre;
    private String direccion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_gerente", unique = true)
    private Gerente gerenteAsociado;
}
