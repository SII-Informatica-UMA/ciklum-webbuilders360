package com.jpa.backend.servicios;

import com.jpa.backend.entities.Gerente;
import com.jpa.backend.repositories.CentroRepository;
import com.jpa.backend.repositories.GerenteRepository;
import com.jpa.backend.repositories.MensajeCentroRepository;
import com.jpa.backend.servicios.excepciones.EntidadExistenteException;
import com.jpa.backend.servicios.excepciones.EntidadNoEncontradaException;

import java.util.List;
import java.util.NoSuchElementException;

public class DBService {
    private GerenteRepository gerenteRepo;
    private CentroRepository centroRepo;
    private MensajeCentroRepository mensajeRepo;

    public DBService(GerenteRepository gerenteRepo, CentroRepository centroRepo, MensajeCentroRepository mensajeRepo){
        this.gerenteRepo = gerenteRepo;
        this.centroRepo = centroRepo;
        this.mensajeRepo = mensajeRepo;
    }

    public List<Gerente> obtenerGerentes(){
        return gerenteRepo.findAll();
    }

    public Gerente obtenerGerente(Long id){
        var gerente = gerenteRepo.findById(id);
        if(gerente.isPresent()){
            return gerente.get();
        }else{
            throw new NoSuchElementException();
        }
    }

    public Long aniadirGerente(Gerente ger){
        if(!gerenteRepo.existsByIdUsuario(ger.getIdUsuario())){
            ger.setId(null);
            gerenteRepo.save(ger);
            return ger.getId();
        }else{
            throw new EntidadExistenteException();
        }
    }

    public void eliminarGerente(Long id){
        if(gerenteRepo.existsById(id)){
            gerenteRepo.deleteById(id);
        }else{
            throw new EntidadNoEncontradaException();
        }
    }

    public void actualizarGerente(Gerente ger){
        if(gerenteRepo.existsById(ger.getId())){
            gerenteRepo.save(ger);
        }else{
            throw new EntidadNoEncontradaException();
        }
    }

}
