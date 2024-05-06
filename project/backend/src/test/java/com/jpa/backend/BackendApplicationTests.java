package com.jpa.backend;

import static org.assertj.core.api.Assertions.assertThat;
import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Destinatario;
import com.jpa.backend.entities.Gerente;
import com.jpa.backend.entities.MensajeCentro;
import com.jpa.backend.repositories.CentroRepository;
import com.jpa.backend.repositories.GerenteRepository;
import com.jpa.backend.repositories.MensajeCentroRepository;
import com.jpa.backend.dtos.CentroDTO;
import com.jpa.backend.dtos.GerenteDTO;
import com.jpa.backend.dtos.MensajeDTO;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
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
					.idUsuario(0L)
					.build();
			
			var peticion = post("http", "localhost", port, "/gerentes", gerenteDTO);
			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:"+port+"/gerentes");
			List<Gerente> gerentesBD = gerenteRepo.findAll();
			assertThat(gerentesBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/"+gerentesBD.get(0).getId());
			compruebaCampos(gerenteDTO.gerente(), gerentesBD.get(0));
		}
		
		@Test
		@DisplayName("inserta correctamente un mensaje")
		public void insertaMensaje(){
			var mensaje = MensajeDTO.builder()
					.asunto("consulta")
					.destinatarios(new ArrayList<>())
					.build();
			var peticion = post("http", "localhost", port, "/mensajes", mensaje);
			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:"+port+"/mensajes");
			List<MensajeCentro> mensajesBD = mensajeRepo.findAll();
			assertThat(mensajesBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/"+mensajesBD.get(0).getId());
			compruebaCampos(mensaje.mensaje(), mensajesBD.get(0));
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

		@Test
		@DisplayName ("devuelve error al modificar un gerente que no existe")
		public void modificarGerenteInexistente(){
			var gerente = GerenteDTO.builder()
					.empresa("EmpresaS.L.")
					.build();
			var peticion = put("http", "localhost", port, "/gerentes/1", gerente);
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName ("devuelve error al modificar un mensaje que no existe")
		public void modificarMensajeInexistente(){
			var mensaje = MensajeDTO.builder()
					.asunto("consulta")
					.destinatarios(new ArrayList<>())
					.build();
			var peticion = put("http", "localhost", port, "/mensajes/1", mensaje);
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName ("devuelve error al modificar un centro que no existe")
		public void modificarCentroInexistente(){
			var centro = CentroDTO.builder()
					.nombre("Gym S.L.")
					.direccion("C/24")
					.build();
			var peticion = put("http", "localhost", port, "/centros/1", centro);
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve un error al eliminar un gerente que no existe")
		public void eliminarGerenteInexistente(){
			var peticion = delete("http", "localhost", port, "/gerentes/1");
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve un error al eliminar un centro que no existe")
		public void eliminarCentroInexistente(){
			var peticion = delete("http", "localhost", port, "/centros/1");
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve un error al eliminar un mensaje que no existe")
		public void eliminarMensajeInexistente(){
			var peticion = delete("http", "localhost", port, "/mensajes/1");
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

	}

	@Nested
	@DisplayName("cuando la base de datos tiene datos")
	public class BaseDatosConDatos{
		
		@BeforeEach
		public void insertarDatos(){
			var gym = new Centro();
			gym.setNombre("Gym");
			gym.setDireccion("C/Malaga");
			centroRepo.save(gym);
		}

		@Test
		@DisplayName("da error cuando se inserta un centro que ya existe")
		public void insertaCentroExistente(){
			var centro = CentroDTO.builder()
				.nombre("Gym")
				.direccion("C/Malaga")
				.build();
			var peticion = post("http", "localhost", port, "/centros", centro);
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
		}

		@Test
		@DisplayName("obtiene un producto concretamente")
		public void errorConCentroConcreto(){
			var peticion = get("http", "localhost", port, "/centros/1");
			var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<CentroDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Gym");
			assertThat(respuesta.getBody().getDireccion()).isEqualTo("C/Malaga");
		}

		@Test
		@DisplayName("modificar un centro correctamente")
		public void modificarCentro(){
			var centro = CentroDTO.builder()
				.nombre("GymNuevo")
				.direccion("C/Teatinos")
				.build();
			var peticion = post("http", "localhost", port, "/centros/1", centro);
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(centroRepo.findById(1L).getNombre()).isEqualTo("GymNuevo");
			assertThat(centroRepo.findById(1L).getDireccion()).isEqualTo("C/Teatinos");

		}

		@Test
		@DisplayName("eliminar un centro correctamente")
		public void eliminarCentro(){
			var centro = new Centro();
			centro.setNombre("GymNuevo");
			centro.setDireccion("C/Teatinos");
			centroRepo.save(centro);
			var peticion = delete("http", "localhost", port, "/centros/2");
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(centroRepo.count()).isEqualTo(1);
		}
	}
}
