package com.jpa.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Destinatario {
    @Id
    @GeneratedValue
    private Integer id;
    private Tipo tipo;

    public Integer getId() {
        return id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
