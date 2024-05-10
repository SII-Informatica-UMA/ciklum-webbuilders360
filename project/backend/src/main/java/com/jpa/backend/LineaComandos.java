package com.jpa.backend;

import com.jpa.backend.repositories.CentroRepository;
import com.jpa.backend.repositories.GerenteRepository;
import com.jpa.backend.repositories.MensajeCentroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class LineaComandos implements CommandLineRunner {
	private final CentroRepository centroRepo;
	private final GerenteRepository gerenteRepo;
	private final MensajeCentroRepository mensajeRepo;

	public LineaComandos(CentroRepository centroRepo, GerenteRepository gerenteRepo, MensajeCentroRepository mensajeRepo) {
		this.centroRepo = centroRepo;
		this.gerenteRepo = gerenteRepo;
		this.mensajeRepo = mensajeRepo;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
	}

}
