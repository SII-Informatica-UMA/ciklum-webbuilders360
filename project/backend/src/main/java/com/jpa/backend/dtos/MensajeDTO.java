package com.jpa.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Destinatario;
import com.jpa.backend.entities.MensajeCentro;
import lombok.*;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@Getter
@Builder
public class MensajeDTO {
    private Long id;
    private Centro centro;
    private String asunto;
    private String contenido;
    private Destinatario remitente;
    private List<Destinatario> copias;
    private List<Destinatario> copiasOcultas;
    private List<Destinatario> destinatarios;
    @JsonProperty("_links")
    private Links links;

    public static MensajeDTO fromMensaje(MensajeCentro mensaje, Function<Long, URI> uriBuilder) {
        return MensajeDTO.builder()
                .id(mensaje.getId())
                .centro(mensaje.getCentro())
                .asunto(mensaje.getAsunto())
                .contenido(mensaje.getContenido())
                .remitente(mensaje.getRemitente())
                .destinatarios(mensaje.getDestinatarios())
                .copias(mensaje.getCopias())
                .copiasOcultas(mensaje.getCopiasOcultas())
                .links(
                        Links.builder()
                                .self(uriBuilder.apply(mensaje.getId()))
                                .build()
                )
                .build();
    }

    public MensajeCentro mensaje() {
        return MensajeCentro.builder()
                .id(id)
                .centro(centro)
                .asunto(asunto)
                .contenido(contenido)
                .remitente(remitente)
                .destinatarios(destinatarios)
                .copias(copias)
                .copiasOcultas(copiasOcultas)
                .build();
    }

}
