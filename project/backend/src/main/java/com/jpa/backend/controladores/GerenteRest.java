package com.jpa.backend.controladores;

import com.jpa.backend.dtos.GerenteDTO;
import com.jpa.backend.entities.Gerente;
import com.jpa.backend.servicios.DBService;

import com.jpa.backend.servicios.excepciones.EntidadExistenteException;
import com.jpa.backend.servicios.excepciones.EntidadNoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/gerentes")
public class GerenteRest {

    private final DBService servicio;

    public GerenteRest(DBService servicio){
        this.servicio = servicio;
    }

    @GetMapping
    public List<GerenteDTO> obtenerTodosLosGerentes(UriComponentsBuilder uriBuilder){
        var gerentes = servicio.obtenerGerentes();
        return gerentes.stream()
                .map(ger->GerenteDTO.fromGerente(ger,
                        gerenteUriBuilder(uriBuilder.build())))
                .toList();
    }

    public static Function<Long, URI> gerenteUriBuilder(UriComponents uriComponents){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().uriComponents(uriComponents);
        return id->uriBuilder.path("/gerentes")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }

    @PostMapping
    public ResponseEntity<?> aniadirGerente(@RequestBody GerenteDTO gerente, UriComponentsBuilder uriBuilder){
        Long id = servicio.aniadirGerente(gerente.gerente());

        return ResponseEntity.created(gerenteUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

    @GetMapping("{id}")
    public GerenteDTO obtenerGerente(@PathVariable Long id, UriComponentsBuilder uriBuilder){
        var gerente = servicio.obtenerGerente(id);
        return GerenteDTO.fromGerente(gerente, gerenteUriBuilder(uriBuilder.build()));
    }

    @PutMapping("{id}")
    public void actualizarGerente(@PathVariable Long id, @RequestBody GerenteDTO gerente){
        Gerente ger = gerente.gerente();
        ger.setId(id);
        servicio.actualizarGerente(ger);
    }

    @DeleteMapping("{id}")
    public void eliminarGerente(@PathVariable Long id){
        servicio.eliminarGerente(id);
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void noEncontrado() {}

    @ExceptionHandler(EntidadExistenteException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void existente() {}
}
