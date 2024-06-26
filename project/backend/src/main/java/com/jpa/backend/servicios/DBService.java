package com.jpa.backend.servicios;

import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Gerente;
import com.jpa.backend.entities.MensajeCentro;
import com.jpa.backend.repositories.CentroRepository;
import com.jpa.backend.repositories.GerenteRepository;
import com.jpa.backend.repositories.MensajeCentroRepository;
import com.jpa.backend.security.SecurityConfiguration;
import com.jpa.backend.servicios.excepciones.EntidadExistenteException;
import com.jpa.backend.servicios.excepciones.EntidadNoEncontradaException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log
public class DBService {
    private final GerenteRepository gerenteRepo;
    private final CentroRepository centroRepo;
    private final MensajeCentroRepository mensajeRepo;

    @Value("${baseURIOfFrontend:http://localhost:4200}")
    private String baseURIOfFrontend;

    @Value("${passwordresettoken.expiration:0}")
    private long passwordResetTokenExpiration;


    //Gerente
    public List<Gerente> obtenerGerentes() {
        return gerenteRepo.findAll();
    }

    public Gerente obtenerGerente(Long id) {
        var gerente = gerenteRepo.findById(id);
        if (gerente.isPresent()) {
            return gerente.get();
        } else {
            throw new EntidadNoEncontradaException();
        }
    }


    public Long aniadirGerente(Gerente ger) {
        Optional<UserDetails> user = SecurityConfiguration.getAuthenticatedUser();
        user.ifPresent(u -> log.info("Usuario autenticado: " + u.getUsername()));

        if (!gerenteRepo.existsByIdUsuario(ger.getIdUsuario())) {
            ger.setId(null);
            gerenteRepo.save(ger);
            return ger.getId();
        } else {
            throw new EntidadExistenteException();
        }
    }

    public void eliminarGerente(Long id) {
        if (gerenteRepo.existsById(id)) {
            gerenteRepo.deleteById(id);
        } else {
            throw new EntidadNoEncontradaException();
        }
    }

    public void actualizarGerente(Gerente gerente) {
        if (gerenteRepo.existsById(gerente.getId())) {
            gerenteRepo.save(gerente);
        } else {
            throw new EntidadNoEncontradaException();
        }
    }

    //Centro
    public List<Centro> obtenerCentros() {
        return centroRepo.findAll();
    }

    public Centro obtenerCentro(long id) {
        Centro centro = centroRepo.findById(id);
        if (centro == null) {
            throw new EntidadNoEncontradaException();
        }
        return centro;
    }

    public Long aniadirCentro(Centro c) {
        Optional<UserDetails> user = SecurityConfiguration.getAuthenticatedUser();
        user.ifPresent(u -> log.info("Usuario autenticado: " + u.getUsername()));

        if (!centroRepo.existsByDireccion(c.getDireccion())) {
            c.setId(null);
            centroRepo.save(c);
            return c.getId();
        } else {
            throw new EntidadExistenteException();
        }
    }

    public void eliminarCentro(Long id) {
        if (centroRepo.existsById(id)) {
            centroRepo.deleteById(id);
        } else {
            throw new EntidadNoEncontradaException();
        }
    }

    public void actualizarCentro(Centro centro) {
        Optional<Centro> aux = centroRepo.findById(centro.getId());
        if (aux.isPresent()) {
            centro.setGerenteAsociado(aux.get().getGerenteAsociado());
            centroRepo.save(centro);
        } else {
            throw new EntidadNoEncontradaException();
        }
    }

    public Gerente obtenerCentroGerente(long centroId) {
        Centro centro = obtenerCentro(centroId);
        return centro.getGerenteAsociado();
    }

    public void asociarCentroGerente(long centroId, long gerenteId) {
        Centro centro = obtenerCentro(centroId);
        Gerente gerente = obtenerGerente(gerenteId);
        centro.setGerenteAsociado(gerente);
        centroRepo.save(centro);
    }

    public void desasociarCentroGerente(long centroId) {
        Centro centro = obtenerCentro(centroId);
        centro.setGerenteAsociado(null);
        centroRepo.save(centro);
    }

    public void desasociarCentroGerente(long centroId, int gerenteId) {
        Centro centro = obtenerCentro(centroId);
        if (centro.getGerenteAsociado() == null || centro.getGerenteAsociado().getId() != gerenteId) {
            throw new EntidadNoEncontradaException();
        }
        centro.setGerenteAsociado(null);
        centroRepo.save(centro);
    }

    //Mensaje
    public List<MensajeCentro> obtenerMensajes() {
        return mensajeRepo.findAll();
    }

    public MensajeCentro obtenerMensaje(Long id) {
        var mensajeCentro = mensajeRepo.findById(id);
        if (mensajeCentro.isPresent()) {
            return mensajeCentro.get();
        } else {
            throw new EntidadNoEncontradaException();
        }
    }

    public Long aniadirMensajeCentro(MensajeCentro m) {
        if (m.getDestinatarios() != null && m.getAsunto() != null) {
            m.setId(null);
            mensajeRepo.save(m);
            return m.getId();
        } else {
            throw new EntidadExistenteException();
        }
    }

    public void eliminarMensajeCentro(Long id) {
        if (mensajeRepo.existsById(id)) {
            mensajeRepo.deleteById(id);
        } else {
            throw new EntidadNoEncontradaException();
        }
    }

    public void actualizarMensajeCentro(MensajeCentro mensaje) {
        if (mensajeRepo.existsById(mensaje.getId())) {
            mensajeRepo.save(mensaje);
        } else {
            throw new EntidadNoEncontradaException();
        }
    }
}
