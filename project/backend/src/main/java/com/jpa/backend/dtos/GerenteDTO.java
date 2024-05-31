package com.jpa.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpa.backend.entities.Gerente;
import lombok.*;

import java.net.URI;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GerenteDTO {
    private Long id;
    private Long idUsuario;
    private String empresa;
    @JsonProperty("_links")
    private Links links;

    public static GerenteDTO fromGerente(Gerente gerente, Function<Long, URI> uriBuilder) {
        var dto = new GerenteDTO();
        dto.setId(gerente.getId());
        dto.setIdUsuario(gerente.getIdUsuario());
        dto.setEmpresa(gerente.getEmpresa());
        dto.setLinks(
                Links.builder()
                        .self(uriBuilder.apply(gerente.getId()))
                        .build());
        return dto;
    }

    public Gerente gerente() {
        var ger = new Gerente();
        ger.setId(id);
        ger.setIdUsuario(idUsuario);
        ger.setEmpresa(empresa);
        return ger;
    }

}
