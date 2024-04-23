package com.jpa.backend.entities;

public class Centro {
    private String nombre;
    private String direccion;
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
}
