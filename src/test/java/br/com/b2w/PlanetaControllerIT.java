package br.com.b2w;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import br.com.b2w.models.Planeta;
import br.com.b2w.repositories.PlanetaRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlanetaControllerIT {

    @LocalServerPort
    private int porta;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private PlanetaRepository planetaRepo;

    @Autowired
    private PlanetaTestModels testModels;

    private String apiBaseUrl;

    @PostConstruct
    void setUp() {
        apiBaseUrl = "http://localhost:" + porta + "/api";
    }

    @AfterEach
    void limparBaseDeTestes() {
        planetaRepo.excluirTodos();
    }

    
    @DisplayName("POST 409 /planetas")
    @Test
    void deveriaDarConflitoAoAdicionarERetornar409Conflict() {
        
        var alderaan = planetaRepo.salvar(testModels.getAlderaan());
        ResponseEntity<Planeta> response = rest.postForEntity(apiBaseUrl + "/planetas", alderaan, Planeta.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @DisplayName("POST 201 /planetas")
    @Test
    void deveriaAdicionarPlanetaERetornar201Created() {
        
        ResponseEntity<Planeta> response = rest.postForEntity(apiBaseUrl + "/planetas", testModels.getAlderaan(), Planeta.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("location")).isNotNull();
    }

    @DisplayName("GET 200 /planetas")
    @Test
    void deveriaListarPlanetasERetornar200Ok() {

        var alderaan = planetaRepo.salvar(testModels.getAlderaan());
        var hoth = planetaRepo.salvar(testModels.getHoth());

        List<Planeta> planetasExistentes = new ArrayList<Planeta>();
        planetasExistentes.add(alderaan);
        planetasExistentes.add(hoth);

        ResponseEntity<List<Planeta>> result = rest.exchange(apiBaseUrl + "/planetas", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Planeta>>() {});
        
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactlyInAnyOrderElementsOf(planetasExistentes);
    }

    @DisplayName("GET 404 /planetas/?id={id}")
    @Test
    void deveriaBuscarPlanetaPorIdERetornar404NotFound() {
        
        ResponseEntity<Planeta> result = rest.getForEntity(apiBaseUrl + "/planetas/?id=222", Planeta.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @DisplayName("GET 200 /planetas/?id={id}")
    @Test
    void deveriaBuscarPlanetaPorIdERetornar200Ok() {
        
        Planeta planetaExistente = planetaRepo.salvar(testModels.getTatooine());
        ResponseEntity<Planeta> result = rest.getForEntity(apiBaseUrl + "/planetas/?id=1", Planeta.class);
        
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(planetaExistente);
    }

    @DisplayName("GET 404 /planetas/?nome={nome}")
    @Test
    void deveriaBuscarPlanetaPorNomeERetornar404NotFound() {
        
        ResponseEntity<Planeta> result = rest.getForEntity(apiBaseUrl + "/planetas/?nome=inexistente", Planeta.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @DisplayName("GET 200 /planetas/?nome={nome}")
    @Test
    void deveriaBuscarPlanetaPorNomeERetornar200Ok() {
        
        Planeta planetaExistente = planetaRepo.salvar(testModels.getTatooine());
        ResponseEntity<Planeta> result = rest.getForEntity(apiBaseUrl + "/planetas/?nome=" + 
            planetaExistente.getNome(), Planeta.class);
        
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(planetaExistente);
    }

    @DisplayName("DELETE 204 /planetas/{nome}")
    @Test
    void deveriaExcluirERetornar204NoContent() {
        Planeta planetaExistente = planetaRepo.salvar(testModels.getAlderaan());
        ResponseEntity<Long> result = rest.exchange(
            apiBaseUrl + "/planetas/" + planetaExistente.getNome(), HttpMethod.DELETE, null,
            new ParameterizedTypeReference<Long>() {});

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}