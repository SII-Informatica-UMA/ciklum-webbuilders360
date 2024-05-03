package com.jpa.backend.dtos;

import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Destinatario;
import com.jpa.backend.entities.MensajeCentro;
import com.jpa.backend.entities.Tipo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

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
public class MensajeDTO {
    private Long id;
    private Centro centro;
    private Long idDestinatario;
    private String asunto;
    private String contenido;
    private Tipo tipo;
    private List<Destinatario> copias;
    private List<Destinatario> copiasOcultas;
    private List<Destinatario> destinatarios;
    @JsonProperty("_links")
    private Links links;

    public static MensajeDTO fromMensajes(MensajeCentro mensaje, Function<Long, URI> uriBuilder){
        var dto = new MensajeDTO();
        dto.setId(mensaje.getId());
        dto.setCentro(mensaje.getCentro());
        dto.setIdDestinatario(mensaje.getIdDestinatario());
        dto.setAsunto(mensaje.getAsunto());
        dto.setContenido(mensaje.getContenido());
        dto.setTipo(mensaje.getTipo());
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

    public MensajeCentro mensaje(){
        var men = new MensajeCentro();
        men.setId(id);
        men.setIdDestinatario(idDestinatario);
        men.setCentro(centro);
        men.setAsunto(asunto);
        men.setContenido(contenido);
        men.setTipo(tipo);
        men.setCopias(copias);
        men.setDestinatarios(destinatarios);
        men.setCopiasOcultas(copiasOcultas);
        return men;
    }

}
