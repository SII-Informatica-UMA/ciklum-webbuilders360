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
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
        var dto = new MensajeDTO();
        dto.setId(mensaje.getId());
        dto.setCentro(mensaje.getCentro());
        dto.setDestinatarios(mensaje.getDestinatarios());
        dto.setAsunto(mensaje.getAsunto());
        dto.setContenido(mensaje.getContenido());
        dto.setRemitente(mensaje.getRemitente());
        dto.setDestinatarios(mensaje.getDestinatarios());
        dto.setCopias(mensaje.getCopias());
        dto.setCopiasOcultas(mensaje.getCopiasOcultas());
        dto.setLinks(
                Links.builder()
                        .self(uriBuilder.apply(mensaje.getId()))
                        .build()
        );
        return dto;
    }

    public MensajeCentro mensaje() {
        var men = new MensajeCentro();
        men.setId(id);
        men.setDestinatarios(destinatarios);
        men.setCentro(centro);
        men.setAsunto(asunto);
        men.setContenido(contenido);
        men.setRemitente(remitente);
        men.setCopias(copias);
        men.setDestinatarios(destinatarios);
        men.setCopiasOcultas(copiasOcultas);
        return men;
    }

}
