package com.jpa.backend.entities;

import java.util.List;
import java.util.Objects;

public class Mensaje {
    String asunto;
    List<Destinatario> destinatarios;
    List<Destinatario> copia;
    List<Destinatario> copiaOculta;
    Destinatario remitente;
    String contenido;
    Integer idMensaje;

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
    public Integer getIdMensaje() {
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
    public void setIdMensaje(Integer idMensaje) {
        this.idMensaje = idMensaje;
    }
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
}
