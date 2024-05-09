package com.jpa.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name= "mensaje")
@Data
public class MensajeCentro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mensaje_centro_seq")
    @SequenceGenerator(name = "mensaje_centro_seq", sequenceName = "mensaje_centro_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_id")
    private Centro centro;

    //@Column(name = "id_destinatario")
    private Long idDestinatario;

    private String asunto;
    private String contenido;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mensaje_centro_copia", joinColumns = @JoinColumn(name = "mensaje_centro_id"))
    @Column(name = "id_destinatario")
    private List<Destinatario> copias;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mensaje_centro_copia_oculta", joinColumns = @JoinColumn(name = "mensaje_centro_id"))
    @Column(name = "id_destinatario")
    private List<Destinatario> copiasOcultas;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mensaje_centro_destinatarios", joinColumns = @JoinColumn(name = "mensaje_centro_id"))
    @Column(name = "id_destinatario")
    private List<Destinatario> destinatarios;
}
