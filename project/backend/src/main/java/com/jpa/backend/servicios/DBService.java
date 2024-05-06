package com.jpa.backend.servicios;

import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Gerente;
import com.jpa.backend.entities.MensajeCentro;
import com.jpa.backend.repositories.CentroRepository;
import com.jpa.backend.repositories.GerenteRepository;
import com.jpa.backend.repositories.MensajeCentroRepository;
import com.jpa.backend.servicios.excepciones.EntidadExistenteException;
import com.jpa.backend.servicios.excepciones.EntidadNoEncontradaException;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class DBService {
    private GerenteRepository gerenteRepo;
    private CentroRepository centroRepo;
    private MensajeCentroRepository mensajeRepo;

    public DBService(GerenteRepository gerenteRepo, CentroRepository centroRepo, MensajeCentroRepository mensajeRepo){
        this.gerenteRepo = gerenteRepo;
        this.centroRepo = centroRepo;
        this.mensajeRepo = mensajeRepo;
    }

    //Gerente
    public List<Gerente> obtenerGerentes(){
        return gerenteRepo.findAll();
    }

    public Gerente obtenerGerente(Long id){
        var gerente = gerenteRepo.findById(id);
        if(gerente.isPresent()){
            return gerente.get();
        }else{
            throw new EntidadNoEncontradaException();
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

    public void actualizarGerente(Gerente gerente){
        if(gerenteRepo.existsById(gerente.getId())){
            gerenteRepo.save(gerente);
        }else{
            throw new EntidadNoEncontradaException();
        }
    }

    //Centro
    public List<Centro> obtenerCentros(){
        return centroRepo.findAll();
    }

    public Centro obtenerCentro(Long id){
        var centro = centroRepo.findById(id);
        if(centro.isPresent()){
            return centro.get();
        }else{
            throw new EntidadNoEncontradaException();
        }
    }

    public Long aniadirCentro(Centro c){
        if(!centroRepo.existsByNombre(c.getNombre()) || !centroRepo.existsByDireccion(c.getDireccion())){
            c.setIdCentro(null);
            centroRepo.save(c);
            return c.getIdCentro();
        }else{
            throw new EntidadExistenteException();
        }
    }

    public void eliminarCentro(Long id){
        if(centroRepo.existsById(id)){
            centroRepo.deleteById(id);
        }else{
            throw new EntidadNoEncontradaException();
        }
    }

    public void actualizarCentro(Centro centro){
        if(centroRepo.existsById(centro.getIdCentro())){
            centroRepo.save(centro);
        }else{
            throw new EntidadNoEncontradaException();
        }
    }

     //Mensaje
     public List<MensajeCentro> obtenerMensajes(){
        return mensajeRepo.findAll();
    }

    public MensajeCentro obtenerMensaje(Long id){
        var mensajeCentro = mensajeRepo.findById(id);
        if(mensajeCentro.isPresent()){
            return mensajeCentro.get();
        }else{
            throw new EntidadNoEncontradaException();
        }
    }
     
    public Long aniadirMensajeCentro(MensajeCentro m){
        if(m.getDestinatarios()!=null && m.getAsunto()!=null){
            m.setId(null);
            mensajeRepo.save(m);
            return m.getId();
        }else{
            throw new EntidadExistenteException();
        }
    }
    
    public void eliminarMensajeCentro(Long id){
        if(mensajeRepo.existsById(id)){
            mensajeRepo.deleteById(id);
        }else{
            throw new EntidadNoEncontradaException();
        }
    }

    public void actualizarMensajeCentro(MensajeCentro mensaje){
        if(mensajeRepo.existsById(mensaje.getId())){
            mensajeRepo.save(mensaje);
        }else{
            throw new EntidadNoEncontradaException();
        }
    }


}
