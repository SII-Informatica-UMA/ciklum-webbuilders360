package com.jpa.backend;

import static org.assertj.core.api.Assertions.assertThat;
import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Gerente;
import com.jpa.backend.entities.MensajeCentro;
import com.jpa.backend.repositories.CentroRepository;
import com.jpa.backend.repositories.GerenteRepository;
import com.jpa.backend.repositories.MensajeCentroRepository;
import com.jpa.backend.dtos.CentroDTO;
import com.jpa.backend.dtos.GerenteDTO;
import com.jpa.backend.dtos.MensajeDTO;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de administracion")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BackendApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	@Value(value = "${local.server.port}")
	private int port;

	@Autowired
	private CentroRepository centroRepo;
	@Autowired
	private GerenteRepository gerenteRepo;
	@Autowired
	private MensajeCentroRepository mensajeRepo;

	private URI uri(String scheme, String host, int port, String ...paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path: paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}

	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.delete(uri)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private void compruebaCampos(Gerente expected, Gerente actual){
		assertThat(actual.getIdUsuario()).isEqualTo(expected.getIdUsuario());
		assertThat(actual.getEmpresa()).isEqualTo(expected.getEmpresa());
		assertThat(actual.getCentrosAsociados()).isEqualTo(expected.getCentrosAsociados());
	}

	private void compruebaCampos(MensajeCentro expected, MensajeCentro actual){
		assertThat(actual.getAsunto()).isEqualTo(expected.getAsunto());
		assertThat(actual.getCentro()).isEqualTo(expected.getCentro());
		assertThat(actual.getContenido()).isEqualTo(expected.getContenido());
		assertThat(actual.getCopias()).isEqualTo(expected.getCopias());
		assertThat(actual.getCopiasOcultas()).isEqualTo(expected.getCopiasOcultas());
		assertThat(actual.getDestinatarios()).isEqualTo(expected.getDestinatarios());
	}

	private void compruebaCampos(Centro expected, Centro actual){
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
		assertThat(actual.getDireccion()).isEqualTo(expected.getDireccion());
		assertThat(actual.getGerenteAsociado()).isEqualTo(expected.getGerenteAsociado());
	}

	@Nested
	@DisplayName("cuando la base de datos esta vacía")
	public class BasesDatosVacía{

		@Test
		@DisplayName("devuelve un error al acceder a un gerente concreto")
		public void errorConGerenteConcreto(){
			var peticion = get("http", "localhost", port, "/gerentes/1");
			var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<GerenteDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		@Test
		@DisplayName("devuelve un error al acceder a un mensaje concreto")
		public void errorConMensajeConcreto(){
			var peticion = get("http", "localhost", port, "/mensajes/1");
			var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<MensajeDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		@Test
		@DisplayName("devuelve un error al acceder a un centro concreto")
		public void errorConCentroConcreto(){
			var peticion = get("http", "localhost", port, "/centros/1");
			var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<CentroDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("inserta correctamente un gerente")
		@Transactional
		public void insertaGerente(){
			var gerenteDTO = GerenteDTO.builder()
					.empresa("GerentesS.L")
					.idUsuario(1001L)
					.centrosAsociados(new ArrayList<Centro>())
					.build();
			
			var peticion = post("http", "localhost", port, "/gerentes", gerenteDTO);
			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:"+port+"/gerentes");
			List<Gerente> gerentesBD = gerenteRepo.findAll();
			gerentesBD.size();
			assertThat(gerentesBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/"+gerentesBD.get(0).getId());
			compruebaCampos(gerenteDTO.gerente(), gerentesBD.get(0));
		}

		@Test
		@DisplayName("inserta correctamente un centro")
		public void insertaCentro(){
			var centro = CentroDTO.builder()
					.nombre("Gym S.L.")
					.direccion("C/24")
					.build();
			var peticion = post("http", "localhost", port, "/centros", centro);
			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:"+port+"/centros");
			List<Centro> centrosBD = centroRepo.findAll();
			assertThat(centrosBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/"+centrosBD.get(0).getIdCentro());
			compruebaCampos(centro.centro(), centrosBD.get(0));
		}

	}
}
