package com.jpa.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpa.backend.entities.Centro;
import lombok.*;

import java.net.URI;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentroDTO {

    private Long id;
    private String nombre;
    private String direccion;
    @JsonProperty("_links")
    private Links links;

    public static CentroDTO fromCentro(Centro centro, Function<Long, URI> uriBuilder) {
        var dto = new CentroDTO();
        dto.setId(centro.getId());
        dto.setNombre(centro.getNombre());
        dto.setDireccion(centro.getDireccion());
        dto.setLinks(
                Links.builder()
                        .self(uriBuilder.apply(centro.getId()))
                        .build());
        return dto;
    }

    public Centro centro() {
        var centro = new Centro();
        centro.setId(id);
        centro.setDireccion(direccion);
        centro.setNombre(nombre);
        return centro;
    }

}
