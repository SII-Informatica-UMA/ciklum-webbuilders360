package com.jpa.backend.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name= "mensaje_centro")
public class MensajeCentro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mensaje_centro_seq")
    @SequenceGenerator(name = "mensaje_centro_seq", sequenceName = "mensaje_centro_seq", allocationSize = 50)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "centro_id")
    private Centro centro;

    @Column(name = "id_destinatario")
    private Long idDestinatario;

    private String asunto;
    private String contenido;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @ElementCollection
    @CollectionTable(name = "mensaje_centro_copia", joinColumns = @JoinColumn(name = "mensaje_centro_id"))
    @Column(name = "id_destinatario")
    private List<Destinatario> copias;

    @ElementCollection
    @CollectionTable(name = "mensaje_centro_copia_oculta", joinColumns = @JoinColumn(name = "mensaje_centro_id"))
    @Column(name = "id_destinatario")
    private List<Destinatario> copiasOcultas;

    @ElementCollection
    @CollectionTable(name = "mensaje_centro_destinatarios", joinColumns = @JoinColumn(name = "mensaje_centro_id"))
    @Column(name = "id_destinatario")
    private List<Destinatario> destinatarios;

    public Long getId() {return id;}
    public Centro getCentro() {return centro;}
    public Long getIdDestinatario() {return idDestinatario;}
    public String getAsunto() {return asunto;}
    public String getContenido() {return contenido;}
    public Tipo getTipo() {return tipo;}
    public List<Destinatario> getCopias() {return copias;}
    public List<Destinatario> getCopiasOcultas() {return copiasOcultas;}
    public List<Destinatario> getDestinatarios() {return destinatarios;}

    public void setId(Long id) {this.id = id;}
    public void setCentro(Centro centro) {this.centro = centro;}
    public void setIdDestinatario(Long idDestinatario) {
        this.idDestinatario = idDestinatario;
    }
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    public void setTipo(Tipo tipo) {this.tipo = tipo;}
    public void setCopias(List<Destinatario> copias) {
        this.copias = copias;
    }
    public void setCopiasOcultas(List<Destinatario> copiasOcultas) {
        this.copiasOcultas = copiasOcultas;
    }
    public void setDestinatarios(List<Destinatario> destinatarios) {
        this.destinatarios = destinatarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MensajeCentro that = (MensajeCentro) o;
        return Objects.equals(id, that.id) && Objects.equals(centro, that.centro) && Objects.equals(idDestinatario, that.idDestinatario) && Objects.equals(asunto, that.asunto) && Objects.equals(contenido, that.contenido) && tipo == that.tipo && Objects.equals(copias, that.copias) && Objects.equals(copiasOcultas, that.copiasOcultas) && Objects.equals(destinatarios, that.destinatarios);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, centro, idDestinatario, asunto, contenido, tipo, copias, copiasOcultas, destinatarios);
    }

    @Override
    public String toString() {
        return "MensajeCentro{" +
                "id=" + id +
                ", centro=" + centro +
                ", idDestinatario=" + idDestinatario +
                ", asunto='" + asunto + '\'' +
                ", contenido='" + contenido + '\'' +
                ", tipo=" + tipo +
                ", copias=" + copias +
                ", copiasOcultas=" + copiasOcultas +
                ", destinatarios=" + destinatarios +
                '}';
    }
}
