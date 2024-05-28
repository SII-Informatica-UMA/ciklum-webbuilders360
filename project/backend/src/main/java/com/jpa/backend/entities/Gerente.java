package com.jpa.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Gerente {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long idUsuario;
    private String empresa;
    @OneToMany (mappedBy = "gerenteAsociado", fetch = FetchType.EAGER, cascade = CascadeType.MERGE) //TODO cambiar relación
    private List<Centro> centrosAsociados;
}
