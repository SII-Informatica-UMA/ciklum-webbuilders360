package com.jpa.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpa.backend.dtos.CentroDTO;
import com.jpa.backend.dtos.GerenteDTO;
import com.jpa.backend.dtos.MensajeDTO;
import com.jpa.backend.entities.Centro;
import com.jpa.backend.entities.Destinatario;
import com.jpa.backend.entities.Gerente;
import com.jpa.backend.entities.MensajeCentro;
import com.jpa.backend.repositories.CentroRepository;
import com.jpa.backend.repositories.GerenteRepository;
import com.jpa.backend.repositories.MensajeCentroRepository;
import com.jpa.backend.security.JwtUtil;
import com.jpa.backend.servicios.DBService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de administracion")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc

class BackendApplicationTests {

    @Value(value = "${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private CentroRepository centroRepo;

    @Autowired
    private GerenteRepository gerenteRepo;


    @Autowired
    private MensajeCentroRepository mensajeRepo;

    @Autowired
    private DBService dbService;

    private static final String SCHEME = "http";
    private static final String HOST = "localhost";

    private String token;


    @BeforeEach
    public void setUp() {
        // Generar token JWT
        UserDetails userDetails = User.withUsername("user").password("password").roles("USER").build();
        token = jwtUtil.generateToken(userDetails);
    }

    private URI uri(int port, String... paths) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(SCHEME)
                .host(HOST).port(port);
        for (String path : paths) {
            ub = ub.path(path);
        }
        return ub.build();
    }

    private RequestEntity<Void> get(int port, String path) {
        URI uri = uri(port, path);
        return RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .build();
    }

    private RequestEntity<Void> delete(int port, String path) {
        URI uri = uri(port, path);
        return RequestEntity.delete(uri)
                .header("Authorization", "Bearer " + token)
                .build();
    }

    private <T> RequestEntity<T> post(int port, String path, T object) {
        URI uri = uri(port, path);
        return RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(object);
    }

    private <T> RequestEntity<T> put(int port, String path, T object) {
        URI uri = uri(port, path);
        return RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(object);
    }

    private void compruebaCampos(Gerente expected, Gerente actual) {
        assertThat(actual.getIdUsuario()).isEqualTo(expected.getIdUsuario());
        assertThat(actual.getEmpresa()).isEqualTo(expected.getEmpresa());
        System.out.println(actual.getCentroAsociado());
        assertEquals(actual.getCentroAsociado(), expected.getCentroAsociado());
    }

    private void compruebaCampos(MensajeCentro expected, MensajeCentro actual) {
        assertThat(actual.getAsunto()).isEqualTo(expected.getAsunto());
        assertThat(actual.getCentro()).isEqualTo(expected.getCentro());
        assertThat(actual.getContenido()).isEqualTo(expected.getContenido());

        assertEquals(new ArrayList<>(
                actual.getCopias() != null ? actual.getCopias() : new ArrayList<>()
        ), new ArrayList<>(
                expected.getCopias() != null ? expected.getCopias() : new ArrayList<>()
        ));

        assertEquals(new ArrayList<>(
                actual.getCopiasOcultas() != null ? actual.getCopiasOcultas() : new ArrayList<>()
        ), new ArrayList<>(
                expected.getCopiasOcultas() != null ? expected.getCopiasOcultas() : new ArrayList<>()
        ));

        assertEquals(new ArrayList<>(
                actual.getDestinatarios() != null ? actual.getDestinatarios() : new ArrayList<>()
        ), new ArrayList<>(
                expected.getDestinatarios() != null ? expected.getDestinatarios() : new ArrayList<>()
        ));
    }

    private void compruebaCampos(Centro expected, Centro actual) {
        assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
        assertThat(actual.getDireccion()).isEqualTo(expected.getDireccion());
        assertThat(actual.getGerenteAsociado()).isEqualTo(expected.getGerenteAsociado());
    }

    @Nested
    @DisplayName("cuando la base de datos esta vacía")
    public class BasesDatosVacia {

        @Test
        @DisplayName("devuelve un error al abtener a un gerente que no existe")
        public void errorConGerenteConcreto() {
            var peticion = get(port, "/gerentes/1");
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<GerenteDTO>() {
            });

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("devuelve un error al obtener a un mensaje que no existe")
        public void errorConMensajeConcreto() {
            var peticion = get(port, "/mensajes/1");
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<MensajeDTO>() {
            });

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("devuelve un error al obtener a un centro que no existe")
        public void errorConCentroConcreto() {
            var peticion = get(port, "/centros/1");
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<CentroDTO>() {
            });

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("inserta correctamente un gerente")
        @Transactional
        public void insertaGerente() {
            GerenteDTO gerenteDTO = GerenteDTO.builder()
                    .empresa("GerentesS.L")
                    .idUsuario(0L)
                    .build();

            var peticion = post(port, "/gerentes", gerenteDTO);
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<GerenteDTO>() {
            });

            assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        }

        @Test
        @DisplayName("inserta correctamente un mensaje")
        public void insertaMensaje() {
            MensajeDTO mensajeDTO = MensajeDTO.builder()
                    .asunto("consulta")
                    .destinatarios(new ArrayList<>())
                    .build();

            var peticion = post(port, "/mensajes", mensajeDTO);
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<MensajeDTO>() {
            });

            assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        }

        @Test
        @DisplayName("inserta correctamente un centro")
        public void insertaCentro() {
            CentroDTO centroDTO = CentroDTO.builder()
                    .nombre("Gym S.L.")
                    .direccion("C/24")
                    .build();

            var peticion = post(port, "/centros", centroDTO);
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<CentroDTO>() {
            });

            assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        }


        @Test
        @DisplayName("devuelve error al modificar un gerente que no existe")
        public void modificarGerenteInexistente() throws Exception {

            GerenteDTO gerenteDTO = GerenteDTO.builder()
                    .empresa("EmpresaS.L.")
                    .build();

            var peticion = put(port, "/gerentes/1", gerenteDTO);
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<GerenteDTO>() {
            });

            assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        }

        @Test
        @DisplayName("devuelve error al modificar un mensaje que no existe")
        public void modificarMensajeInexistente() {
            MensajeDTO mensajeDTO = MensajeDTO.builder()
                    .asunto("consulta")
                    .destinatarios(new ArrayList<>())
                    .build();

            var peticion = put(port, "/mensajes/1", mensajeDTO);
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<MensajeDTO>() {
            });

            assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        }

        @Test
        @DisplayName("devuelve error al modificar un centro que no existe")
        public void modificarCentroInexistente() {
            CentroDTO centroDTO = CentroDTO.builder()
                    .nombre("Gym S.L.")
                    .direccion("C/24")
                    .build();

            var peticion = put(port, "/centros/1", centroDTO);
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<CentroDTO>() {
            });

            assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        }

        @Test
        @DisplayName("devuelve un error al eliminar un gerente que no existe")
        public void eliminarGerenteInexistente() {
            var peticion = delete(port, "/gerentes/1");
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<GerenteDTO>() {
            });

            assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve un error al eliminar un centro que no existe")
        public void eliminarCentroInexistente() {
            var peticion = delete(port, "/centros/1");
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<CentroDTO>() {
            });

            assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve un error al eliminar un mensaje que no existe")
        public void eliminarMensajeInexistente() {
            var peticion = delete(port, "/mensajes/1");
            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<MensajeDTO>() {
            });

            assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }
    }


    @Nested
    @DisplayName("cuando la base de datos tiene datos")
    public class BaseDatosConDatos {

        @BeforeEach
        public void insertarDatos() {
            Centro gym = new Centro();
            gym.setNombre("Gym");
            gym.setDireccion("C/Malaga");
            centroRepo.save(gym);

            var gerente = new Gerente();
            gerente.setEmpresa("EmpresaS.L.");
            gerente.setIdUsuario(0L);
            gerenteRepo.save(gerente);

            var mensaje = new MensajeCentro();
            mensaje.setAsunto("Prueba");
            mensaje.setContenido("mensaje de prueba");
            mensajeRepo.save(mensaje);

		}
        
        @Test
        @DisplayName("da error cuando se inserta un centro que ya existe (mismo nombre y direccion)")
        public void insertaCentroExistente(){
            var centro = CentroDTO.builder()
                .nombre("Gym")
                .direccion("C/Malaga")
                .build();
            var peticion = post(port, "/centros", centro);
	        var respuesta = restTemplate.exchange(peticion, Void.class);
            assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
        }
        
        @Test
        @DisplayName("da error cuando se inserta un gerente que ya existe")
        public void insertaGerenteExistente(){
            var gerente = GerenteDTO.builder()
                .empresa("EmpresaS.L.")
                .idUsuario(0L)
                .build();
            var peticion = post(port, "/gerentes", gerente);
            var respuesta = restTemplate.exchange(peticion, Void.class);
            assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
        }

    	@Test
        @DisplayName("da error cuando se inserta un mensaje que ya existe")
        public void insertaMensajeExistente(){
        	var mensaje = MensajeDTO.builder()
                .asunto("Prueba")
                .contenido("mensaje de prueba")
                .build();
            var peticion = post(port, "/mensajes", mensaje);
            var respuesta = restTemplate.exchange(peticion, Void.class);
            assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
        }

        @Test
        @DisplayName("da error cuando se inserta un mensaje que sí tiene destinatarios pero no tiene asunto")
        public void insertaMensajeSinAsuntoYConDestinatario(){
            Destinatario dst = new Destinatario();
            dst.setId(1L);
            List<Destinatario> listaDst = new ArrayList<>();
            listaDst.add(dst);
            var mensaje = MensajeDTO.builder()
                    .destinatarios(new ArrayList<Destinatario>(listaDst))
                    .build();
            var peticion = post(port, "/mensajes", mensaje);
            var respuesta = restTemplate.exchange(peticion, Void.class);
            assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
        }

        @Test
    	@DisplayName("modificar un mensaje existente correctamente")
        public void modificarMensaje(){
            var mensaje = MensajeDTO.builder()
                   .id(1L)
                   .asunto("Duda entrenamiento")
                   .contenido("Duda sobre horarios")
                   .build();
            var peticion = put(port, "/mensajes/1", mensaje);
    		var respuesta = restTemplate.exchange(peticion, Void.class);
            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(mensajeRepo.findById(1L).getId()).isEqualTo(1L);
            assertThat(mensajeRepo.findById(1L).getAsunto()).isEqualTo("Duda entrenamiento");
        }

        @Test
        @DisplayName("modificar un centro existente correctamente")
        public void modificarCentro(){
            var centro = CentroDTO.builder()
               .nombre("GymNuevo")
               .direccion("C/Teatinos")
               .build();
            var peticion = put(port, "/centros/1", centro);
            var respuesta = restTemplate.exchange(peticion, Void.class);
            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(centroRepo.findById(1L).getNombre()).isEqualTo("GymNuevo");
            assertThat(centroRepo.findById(1L).getDireccion()).isEqualTo("C/Teatinos");
        }

        @Test
        @DisplayName("modificar un gerente existente correctamente")
        public void modificarGerente(){
        	var gerente = GerenteDTO.builder()
                .id(1L)
                .empresa("Gym Teatinos")
                .idUsuario(1L)
                .build();
            var peticion = put(port, "/gerentes/1", gerente);
            var respuesta = restTemplate.exchange(peticion, Void.class);
            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(gerenteRepo.findById(1L).getId()).isEqualTo(1L);
        	assertThat(gerenteRepo.findById(1L).getEmpresa()).isEqualTo("Gym Teatinos");
        }

       @Test
       @DisplayName("eliminar un centro correctamente")
       public void eliminarCentro(){
           var centro = new Centro();
           centro.setNombre("GymNuevo");
           centro.setDireccion("C/Teatinos");
           centroRepo.save(centro);
           var peticion = delete(port, "/centros/2");
           var respuesta = restTemplate.exchange(peticion, Void.class);
           assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
           assertThat(centroRepo.count()).isEqualTo(1);
        }

    	@Test
        @DisplayName("eliminar un gerente correctamente")
        public void eliminarGerente(){
           var gerente = new Gerente();
           gerente.setEmpresa("EmpresaNuevaS.L.");
           gerente.setIdUsuario(1L);
           gerenteRepo.save(gerente);
           var peticion = delete(port, "/gerentes/2");
           var respuesta = restTemplate.exchange(peticion, Void.class);
           assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
           assertThat(gerenteRepo.count()).isEqualTo(1);
    	}

    	@Test
        @DisplayName("eliminar un mensaje correctamente")
        public void eliminarMensaje(){
            var mensaje = new MensajeCentro();
            mensaje.setAsunto("MensajeEliminar");
            mensaje.setContenido("mensaje a eliminar");
            mensajeRepo.save(mensaje);
            var peticion = delete(port, "/mensajes/2");
                   var respuesta = restTemplate.exchange(peticion, Void.class);
                   assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
                   assertThat(mensajeRepo.count()).isEqualTo(1);
               }
        @Test
        @DisplayName("obtiene un centro concreto")
        public void obtenerCentroConcreto() { 
			var peticion = get(port, "/centros/1");
			var respuesta = restTemplate.exchange(peticion, 
					new ParameterizedTypeReference<CentroDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(Objects.requireNonNull(respuesta.getBody()).getNombre()).isEqualTo("Gym");
			assertThat(respuesta.getBody().getDireccion()).isEqualTo("C/Malaga");
		}

		@Test
		@DisplayName("obtiene un gerente concreto")
		public void obtenerGerenteConcreto(){ 
			var peticion = get(port, "gerentes/1");
			var respuesta = restTemplate.exchange(peticion, 
					new ParameterizedTypeReference<GerenteDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(Objects.requireNonNull(respuesta.getBody()).getEmpresa()).isEqualTo("EmpresaS.L.");
			assertThat(respuesta.getBody().getIdUsuario()).isEqualTo(0L);
		}

		@Test
		@DisplayName("obtiene un mensaje concreto")
		public void obtenerMensajeConcreto(){
			var peticion = get(port, "/mensajes/1");
			var respuesta = restTemplate.exchange(peticion, 
					new ParameterizedTypeReference<MensajeDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(Objects.requireNonNull(respuesta.getBody()).getAsunto()).isEqualTo("Prueba");
			assertThat(respuesta.getBody().getContenido()).isEqualTo("mensaje de prueba");
		}
 
		@Test
		@DisplayName("devuelve una lista de centros")
		public void devuelveListaCentros() {
			
            RequestEntity<Void> peticion = get(port, "/centros");
            ResponseEntity<List<Centro>> respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Centro>>() {
            });

            assertEquals(HttpStatus.OK, respuesta.getStatusCode());

            List<Centro> centros = respuesta.getBody();
            assertThat(centros).isNotNull();
            assertThat(Objects.requireNonNull(respuesta.getBody()).size()).isEqualTo(1);

        }


        @Test
        @DisplayName("devuelve una lista de gerentes")
        public void devuelveListaGerentes() {
			
            RequestEntity<Void> peticion = get(port, "/gerentes");
            ResponseEntity<List<Gerente>> respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Gerente>>() {
            });

            assertEquals(HttpStatus.OK, respuesta.getStatusCode());

            List<Gerente> gerentes = respuesta.getBody();
            assertThat(gerentes).isNotNull();
            assertThat(Objects.requireNonNull(respuesta.getBody()).size()).isEqualTo(1);

        }

        @Test
        @DisplayName("devuelve una lista de mensajes")
        public void devuelveListaMensajes() {
			
            RequestEntity<Void> peticion = get(port, "/mensajes");
            ResponseEntity<List<MensajeCentro>> respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<MensajeCentro>>() {
            });

            assertEquals(HttpStatus.OK, respuesta.getStatusCode());

            List<MensajeCentro> mensajes = respuesta.getBody();
            assertThat(mensajes).isNotNull();
            assertThat(Objects.requireNonNull(respuesta.getBody()).size()).isEqualTo(1);

        }

        @Test
        @DisplayName("asigna correctamente un centro a un gerente")
        public void asignarGerenteIndicandoCentro() {

            var centroDTO = CentroDTO.builder().id(1L).build();

            var centro = centroDTO.centro();

            // Preparamos el producto a insertar
            var gerenteDTO = GerenteDTO.builder()
                    .empresa("EmpresaNV")
            //        .centroAsociado(centro)
                    .idUsuario(1L)
                    .build();

            var gerente = gerenteDTO.gerente();
            centro.setGerenteAsociado(gerente);


            // Preparamos la petición con el centro dentro
            var peticion = post(port, "/gerentes", gerenteDTO);

            // Invocamos al servicio REST
            var respuesta = restTemplate.exchange(peticion, Void.class);

            List<Gerente> gerentesBD = gerenteRepo.findAll();
            Gerente ger = gerentesBD.stream()
                    .filter(p -> p.getEmpresa().equals("EmpresaNV"))
                    .findFirst()
                    .get();


            // Comprobamos el resultado
            assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
            assertThat(Objects.requireNonNull(respuesta.getHeaders().get("Location")).getFirst())
                    .startsWith("http://localhost:" + port + "/gerentes");


            assertThat(gerentesBD).hasSize(2);
            assertThat(Objects.requireNonNull(respuesta.getHeaders().get("Location")).getFirst())
                    .endsWith("/" + ger.getId());
            compruebaCampos(gerenteDTO.gerente(), ger);
        }

    }

}
