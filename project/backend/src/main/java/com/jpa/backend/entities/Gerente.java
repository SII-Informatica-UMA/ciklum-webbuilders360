package com.jpa.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Gerente {
    @Column(nullable = false)
    private Long idUsuario;
    private String empresa;
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany (mappedBy = "gerenteAsociado", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Centro> centrosAsociados;
}
