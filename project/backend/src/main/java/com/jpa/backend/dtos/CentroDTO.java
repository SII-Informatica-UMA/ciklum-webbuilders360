package com.jpa.backend.dtos;

import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Gerente;

import java.net.URI;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentroDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private Gerente gerenteAsociado;
    @JsonProperty("_links")
    private Links links;

    public static CentroDTO fromCentro(Centro centro, Function<Long, URI> uriBuilder) {
        return CentroDTO.builder()
                .id(centro.getId())
                .nombre(centro.getNombre())
                .direccion(centro.getDireccion())
                .gerenteAsociado(centro.getGerenteAsociado())
                .links(
                        Links.builder()
                                .self(uriBuilder.apply(centro.getId()))
                                .build()
                )
                .build();
    }

    public Centro centro() {
        return Centro.builder()
                .id(id)
                .direccion(direccion)
                .nombre(nombre)
                .gerenteAsociado(gerenteAsociado)
                .build();
    }

}
