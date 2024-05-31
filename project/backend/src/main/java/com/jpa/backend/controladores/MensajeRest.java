package com.jpa.backend.controladores;

import com.jpa.backend.dtos.MensajeDTO;
import com.jpa.backend.entities.MensajeCentro;
import com.jpa.backend.servicios.DBService;
import com.jpa.backend.servicios.excepciones.EntidadExistenteException;
import com.jpa.backend.servicios.excepciones.EntidadNoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/mensajes")
public class MensajeRest {

    private final DBService servicio;

    public MensajeRest(DBService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<MensajeDTO> obtenerTodosLosMensajes(UriComponentsBuilder uriBuilder) {
        var mensajes = servicio.obtenerMensajes();
        return mensajes.stream()
                .map(m -> MensajeDTO.fromMensaje(m,
                        mensajesUriBuilder(uriBuilder.build())))
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> aniadirMensajeCentro(@RequestBody MensajeDTO mensaje, UriComponentsBuilder uriBuilder) {
        Long id = servicio.aniadirMensajeCentro(mensaje.mensaje());
        return ResponseEntity.created(mensajesUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

    public static Function<Long, URI> mensajesUriBuilder(UriComponents uriComponents) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().uriComponents(uriComponents);
        return id -> uriBuilder.path("/mensajes")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }


    @GetMapping("{id}")
    public MensajeDTO obtenerMensajeCentro(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        var mensaje = servicio.obtenerMensaje(id);
        return MensajeDTO.fromMensaje(mensaje, mensajesUriBuilder(uriBuilder.build()));
    }

    @PutMapping("{id}")
    public void actualizarMensajeCentro(@PathVariable Long id, @RequestBody MensajeDTO mensaje) {
        MensajeCentro m = mensaje.mensaje();
        m.setId(id);
        servicio.actualizarMensajeCentro(m);
    }

    @DeleteMapping("{id}")
    public void eliminarMensajeCentro(@PathVariable Long id) {
        servicio.eliminarMensajeCentro(id);
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void noEncontrado() {
    }

    @ExceptionHandler(EntidadExistenteException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void existente() {
    }
}
