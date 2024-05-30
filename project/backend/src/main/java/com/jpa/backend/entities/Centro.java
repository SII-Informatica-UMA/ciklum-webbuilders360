package com.jpa.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Centro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "centro_seq")
    @SequenceGenerator(name = "centro_seq", sequenceName = "centro_seq")
    private Long id;
    private String nombre;
    private String direccion;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_gerente", unique = true)
    private Gerente gerenteAsociado;
}
