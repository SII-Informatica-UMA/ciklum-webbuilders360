package com.jpa.backend.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Embeddable
public class Destinatario {
    @Column(name = "id_destinatario")
    private Long id;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    public Long getId() { return id; }
    public Tipo getTipo() { return tipo; }
    public void setId(Long id) { this.id = id; }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destinatario that = (Destinatario) o;
        return Objects.equals(id, that.id) && tipo == that.tipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo);
    }

    @Override
    public String toString() {
        return "Destinatario{" +
                "id=" + id +
                ", tipo=" + tipo +
                '}';
    }
}
