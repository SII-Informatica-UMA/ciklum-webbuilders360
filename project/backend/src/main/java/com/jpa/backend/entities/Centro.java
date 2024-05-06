package com.jpa.backend.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Centro {
    private String nombre;
    private String direccion;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "centro_seq")
    @SequenceGenerator(name = "centro_seq", sequenceName = "centro_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_gerente", unique = true)
    private Gerente gerenteAsociado;

    public String getNombre() {
        return nombre;
    }
    public String getDireccion() {
        return direccion;
    }
    public Long getIdCentro() {
        return id;
    }
    public Gerente getGerenteAsociado(){return gerenteAsociado;}

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public void setIdCentro(Long idCentro) {
        this.id = idCentro;
    }
    public void setGerenteAsociado(Gerente gerenteAsociado) {
        this.gerenteAsociado = gerenteAsociado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Centro centro = (Centro) o;
        return Objects.equals(nombre, centro.nombre) && Objects.equals(direccion, centro.direccion) && Objects.equals(id, centro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, direccion, id);
    }

    @Override
    public String toString() {
        return "Centro{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", idCentro=" + id +
                '}';
    }
}
