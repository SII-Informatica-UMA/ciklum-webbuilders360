package com.jpa.backend.controladores;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

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

import com.jpa.backend.dtos.CentroDTO;
import com.jpa.backend.dtos.GerenteDTO;
import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Gerente;
import com.jpa.backend.servicios.DBService;
import com.jpa.backend.servicios.excepciones.EntidadExistenteException;
import com.jpa.backend.servicios.excepciones.EntidadNoEncontradaException;

@RestController
@RequestMapping("/centros")
public class CentroRest {
     private DBService servicio;

    public CentroRest(DBService servicio){
        this.servicio = servicio;
    }

    @GetMapping
    public List<CentroDTO> obtenerTodosLosCentros(UriComponentsBuilder uriBuilder){
        var centros = servicio.obtenerCentros();
        return centros.stream()
                .map(c->CentroDTO.fromCentro(c,
                        centroUriBuilder(uriBuilder.build())))
                .toList();
    }

    public static Function<Long, URI> centroUriBuilder(UriComponents uriComponents){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().uriComponents(uriComponents);
        return id->uriBuilder.path("/centros")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }

    @PostMapping
    public ResponseEntity<?> aniadirCentro(@RequestBody CentroDTO centro, UriComponentsBuilder uriBuilder){
        Long id = servicio.aniadirCentro(centro.centro());
        return ResponseEntity.created(centroUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

    @GetMapping("{id}")
    public CentroDTO obtenerCentro(@PathVariable Long id, UriComponentsBuilder uriBuilder){
        var centro = servicio.obtenerCentro(id);
        return CentroDTO.fromCentro(centro, centroUriBuilder(uriBuilder.build()));
    }

    @PutMapping("{id}")
    public void actualizarCentro(@PathVariable Long id, @RequestBody CentroDTO centro){
        Centro c = centro.centro();
        c.setId(id);
        servicio.actualizarCentro(c);
    }

    @DeleteMapping("{id}")
    public void eliminarCentro(@PathVariable Long id){
        servicio.eliminarCentro(id);
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void noEncontrado() {}

    @ExceptionHandler(EntidadExistenteException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void existente() {}
}
