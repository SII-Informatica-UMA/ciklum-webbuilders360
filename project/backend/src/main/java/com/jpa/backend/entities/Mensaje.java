package com.jpa.backend.entities;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;



public class Mensaje {
    /*@Id
    @GeneratedValue
    private Long idMensaje;
    String asunto;
    @OneToMany
    @JoinColumn(name = "destinatarios_fk")
    private List<Destinatario> destinatarios;
    @OneToMany
    @JoinColumn(name = "copia_fk")
    private List<Destinatario> copia;
    @OneToMany
    @JoinColumn(name = "copiaOculta_fk")
    private List<Destinatario> copiaOculta;
    @OneToOne
    @JoinColumn(name = "remitente_fk")
    private Destinatario remitente;
    private String contenido;

    public String getAsunto() {
        return asunto;
    }
    public List<Destinatario> getCopia() {
        return copia;
    }
    public List<Destinatario> getCopiaOculta() {
        return copiaOculta;
    }
    public Destinatario getRemitente() {
        return remitente;
    }
    public String getContenido() {
        return contenido;
    }
    public Long getIdMensaje() {
        return idMensaje;
    }
    
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }
    public void setDestinatarios(List<Destinatario> destinatarios) {
        this.destinatarios = destinatarios;
    }
    public void setCopia(List<Destinatario> copia) {
        this.copia = copia;
    }
    public void setCopiaOculta(List<Destinatario> copiaOculta) {
        this.copiaOculta = copiaOculta;
    }
    public void setRemitente(Destinatario remitente) {
        this.remitente = remitente;
    }
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    public void setIdMensaje(Long idMensaje) { this.idMensaje = idMensaje; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mensaje mensaje = (Mensaje) o;
        return Objects.equals(asunto, mensaje.asunto) && Objects.equals(destinatarios, mensaje.destinatarios) && Objects.equals(copia, mensaje.copia) && Objects.equals(copiaOculta, mensaje.copiaOculta) && Objects.equals(remitente, mensaje.remitente) && Objects.equals(contenido, mensaje.contenido) && Objects.equals(idMensaje, mensaje.idMensaje);
    }
    @Override
    public int hashCode() {
        return Objects.hash(asunto, destinatarios, copia, copiaOculta, remitente, contenido, idMensaje);
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "asunto='" + asunto + '\'' +
                ", destinatarios=" + destinatarios +
                ", copia=" + copia +
                ", copiaOculta=" + copiaOculta +
                ", remitente=" + remitente +
                ", contenido='" + contenido + '\'' +
                ", idMensaje=" + idMensaje +
                '}';
    }
     */
}
