package com.jpa.backend.dtos;

import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Gerente;

import java.net.URI;
import java.util.List;
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
public class GerenteDTO {
    private Long id;
    private Long idUsuario;
    private String empresa;
    private List<Centro> centrosAsociados;
    @JsonProperty("_links")
    private Links links;

    public static GerenteDTO fromGerente(Gerente gerente, Function<Long, URI> uriBuilder) {
        return GerenteDTO.builder()
                .id(gerente.getId())
                .idUsuario(gerente.getIdUsuario())
                .empresa(gerente.getEmpresa())
                .centrosAsociados(gerente.getCentrosAsociados())
                .links(
                        Links.builder()
                                .self(uriBuilder.apply(gerente.getId()))
                                .build()
                )
                .build();
    }

    public Gerente gerente() {
        return Gerente.builder()
                .id(id)
                .idUsuario(idUsuario)
                .empresa(empresa)
                .centrosAsociados(centrosAsociados)
                .build();
    }

}
