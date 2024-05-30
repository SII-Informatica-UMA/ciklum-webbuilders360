package com.jpa.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
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
    @OneToOne (mappedBy = "gerenteAsociado", fetch = FetchType.EAGER, cascade = CascadeType.MERGE) //TODO cambiar relación
    private Centro centroAsociado;
}
