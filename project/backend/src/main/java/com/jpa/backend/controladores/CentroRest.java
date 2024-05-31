package com.jpa.backend.controladores;

import com.jpa.backend.dtos.CentroDTO;
import com.jpa.backend.dtos.IdGerenteDTO;
import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Gerente;
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
@RequestMapping("/centros")
public class CentroRest {
    private final DBService servicio;

    public CentroRest(DBService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<CentroDTO> obtenerTodosLosCentros(UriComponentsBuilder uriBuilder) {
        var centros = servicio.obtenerCentros();
        return centros.stream()
                .map(c -> CentroDTO.fromCentro(c,
                        centroUriBuilder(uriBuilder.build())))
                .toList();
    }

    public static Function<Long, URI> centroUriBuilder(UriComponents uriComponents) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().uriComponents(uriComponents);
        return id -> uriBuilder.path("/centros")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }

    @PostMapping
    public ResponseEntity<?> aniadirCentro(@RequestBody CentroDTO centro, UriComponentsBuilder uriBuilder) {
        Long id = servicio.aniadirCentro(centro.centro());
        return ResponseEntity.created(centroUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

    @GetMapping("{id}")
    public CentroDTO obtenerCentro(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        var centro = servicio.obtenerCentro(id);
        return CentroDTO.fromCentro(centro, centroUriBuilder(uriBuilder.build()));
    }

    @PutMapping("{id}")
    public void actualizarCentro(@PathVariable Long id, @RequestBody CentroDTO centro) {
        Centro c = centro.centro();
        c.setId(id);
        servicio.actualizarCentro(c);
    }

    @DeleteMapping("{id}/gerente")
    public void desasociarCentroGerente(@PathVariable long id) {
        servicio.desasociarCentroGerente(id);
    }

    @DeleteMapping(path = "{id}/gerente", params = "gerente")
    public void desasociarCentroGerente(@PathVariable long id, @RequestParam int gerente) {
        servicio.desasociarCentroGerente(id, gerente);
    }

    @GetMapping("{id}/gerente")
    public IdGerenteDTO obtenerCentroGerente(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        Gerente gerente = servicio.obtenerCentroGerente(id);
        return IdGerenteDTO.fromLong(gerente.getId(), centroUriBuilder(uriBuilder.build()));
    }

    @PutMapping("{id}/gerente")
    public void asociarCentroGerente(@PathVariable Long id, @RequestBody IdGerenteDTO idGerenteDTO) {
        servicio.asociarCentroGerente(id, idGerenteDTO.aLong());
    }

    @DeleteMapping("{id}")
    public void eliminarCentro(@PathVariable Long id) {
        servicio.eliminarCentro(id);
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
