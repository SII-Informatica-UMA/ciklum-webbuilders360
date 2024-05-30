package com.jpa.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "mensaje")
@Data
@NoArgsConstructor
public class MensajeCentro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mensaje_centro_seq")
    @SequenceGenerator(name = "mensaje_centro_seq", sequenceName = "mensaje_centro_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_id")
    private Centro centro;

    private String asunto;
    private String contenido;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_remitente")
    private Destinatario remitente;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mensaje_centro_destinatarios", joinColumns = @JoinColumn(name = "mensaje_centro_id"))
    @Column(name = "id_destinatario")
    private List<Destinatario> destinatarios;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mensaje_centro_copia", joinColumns = @JoinColumn(name = "mensaje_centro_id"))
    @Column(name = "id_destinatario")
    private List<Destinatario> copias;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mensaje_centro_copia_oculta", joinColumns = @JoinColumn(name = "mensaje_centro_id"))
    @Column(name = "id_destinatario")
    private List<Destinatario> copiasOcultas;
}
