package com.jpa.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Centro {
    private String nombre;
    private String direccion;
    @Id
    @GeneratedValue
    private Integer idCentro;

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public Integer getIdCentro() {
        return idCentro;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setIdCentro(Integer idCentro) {
        this.idCentro = idCentro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Centro centro = (Centro) o;
        return Objects.equals(nombre, centro.nombre) && Objects.equals(direccion, centro.direccion) && Objects.equals(idCentro, centro.idCentro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, direccion, idCentro);
    }

    @Override
    public String toString() {
        return "Centro{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", idCentro=" + idCentro +
                '}';
    }
}
