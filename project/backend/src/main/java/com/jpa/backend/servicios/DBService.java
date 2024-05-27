package com.jpa.backend.servicios;

import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Gerente;
import com.jpa.backend.entities.MensajeCentro;
import com.jpa.backend.repositories.CentroRepository;
import com.jpa.backend.repositories.GerenteRepository;
import com.jpa.backend.repositories.MensajeCentroRepository;
import com.jpa.backend.security.JwtUtil;
import com.jpa.backend.security.SecurityConfguration;
import com.jpa.backend.servicios.excepciones.EntidadExistenteException;
import com.jpa.backend.servicios.excepciones.EntidadNoEncontradaException;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class DBService {
    private GerenteRepository gerenteRepo;
    private CentroRepository centroRepo;
    private MensajeCentroRepository mensajeRepo;

    private final RestTemplate restTemplate;

    //private final JwtUtil jwtUtil;
    private final Logger log =Logger.getLogger(DBService.class.getName());

    @Value("${baseURIOfFrontend:http://localhost:4200}")
    private String baseURIOfFrontend;

    @Value("${passwordresettoken.expiration:0}")
    private long passwordResetTokenExpiration;

    /*public DBService(GerenteRepository gerenteRepo, CentroRepository centroRepo, 
                        MensajeCentroRepository mensajeRepo, JwtUtil jwtUtil){
        this.gerenteRepo = gerenteRepo;
        this.centroRepo = centroRepo;
        this.mensajeRepo = mensajeRepo;
        this.jwtUtil = jwtUtil;
    }*/

    public DBService(GerenteRepository gerenteRepo, CentroRepository centroRepo, 
                         MensajeCentroRepository mensajeRepo, RestTemplate restTemplate){
    this.gerenteRepo = gerenteRepo;
    this.centroRepo = centroRepo;
    this.mensajeRepo = mensajeRepo;
    this.restTemplate = restTemplate;
    }

    //Gerente
    public List<Gerente> obtenerGerentes(){
        return gerenteRepo.findAll();
    }

    /*public Gerente obtenerGerente(Long id){
        var gerente = gerenteRepo.findById(id);
        if(gerente.isPresent()){
            return gerente.get();
        }else{
            throw new EntidadNoEncontradaException();
        }
    }*/

    public Gerente obtenerGerente(Long id){
         ResponseEntity<Gerente> resp = 
          restTemplate.getForEntity("http://localhost:8080/gerente/" + id, Gerente.class);

        return resp.getStatusCode() == HttpStatus.OK ? resp.getBody() : null;
    }


    public Long aniadirGerente(Gerente ger){
        Optional<UserDetails> user = SecurityConfguration.getAuthenticatedUser();
        user.ifPresent(u -> log.info("Usuario autenticado: " + u.getUsername()));

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
        Optional<UserDetails> user = SecurityConfguration.getAuthenticatedUser();
        user.ifPresent(u -> log.info("Usuario autenticado: " + u.getUsername()));

        if(!centroRepo.existsByNombre(c.getNombre()) || !centroRepo.existsByDireccion(c.getDireccion())){
            c.setId(null);
            centroRepo.save(c);
            return c.getId();
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
        if(centroRepo.existsById(centro.getId())){
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
