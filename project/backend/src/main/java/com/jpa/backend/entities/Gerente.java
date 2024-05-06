package com.jpa.backend.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Gerente {
    @Column(nullable = false)
    private Long idUsuario;
    private String empresa;
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany (mappedBy = "gerenteAsociado", fetch = FetchType.EAGER)
    private List<Centro> centrosAsociados;

    public Long getIdUsuario() {return idUsuario;}
    public String getEmpresa() {return empresa;}

    public Long getId() {return id;}
    public List<Centro> getCentrosAsociados() {return centrosAsociados;}

    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario;}
    public void setEmpresa(String empresa) {this.empresa = empresa;}
    public void setId(Long id) {this.id = id;}
    public void setCentrosAsociados(List<Centro> centrosAsociados) {
        this.centrosAsociados = centrosAsociados;
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
