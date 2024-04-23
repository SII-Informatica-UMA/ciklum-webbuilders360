package com.jpa.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Gerente {
    private Integer idUsuario;
    private String empresa;
    private Integer id;

    @Column(nullable = false)
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gerente gerente = (Gerente) o;
        return Objects.equals(idUsuario, gerente.idUsuario) && Objects.equals(empresa, gerente.empresa) && Objects.equals(id, gerente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, empresa, id);
    }

    @Override
    public String toString() {
        return "Gerente{" +
                "idUsuario=" + idUsuario +
                ", empresa='" + empresa + '\'' +
                ", id=" + id +
                '}';
    }
}
