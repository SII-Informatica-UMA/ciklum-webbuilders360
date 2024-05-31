package com.jpa.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.net.URI;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdGerenteDTO {
    private Long id;
    @JsonProperty("_links")
    private Links links;

    public static IdGerenteDTO fromLong(Long id, Function<Long, URI> uriBuilder) {
        var dto = new IdGerenteDTO();
        dto.setId(id);
        dto.setLinks(
                Links.builder()
                        .self(uriBuilder.apply(id))
                        .build());
        return dto;
    }

    public Long aLong() {
        return id;
    }
}
