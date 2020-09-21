package br.com.b2w.controllers;

import java.net.URI;

import javax.validation.Valid;

import br.com.b2w.models.Planeta;
import br.com.b2w.models.querystring.PlanetaQueryString;
import br.com.b2w.services.PlanetaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Requisitos:
 * - A API deve ser REST
 * - Para cada planeta, os seguintes dados devem ser obtidos do banco de dados 
 *   da aplicação, sendo inserido manualmente:
 *      - Nome
 *      - Clima
 *      - Terreno
 * 
 * - Para cada planeta também devemos ter a quantidade de aparições em filmes, 
 * que podem ser obtidas pela API pública do Star Wars: https://swapi.dev/about
 * 
 * Funcionalidades desejadas:
 * 
 * - Adicionar um planeta (com nome, clima e terreno)
 * - Listar planetas
 * - Buscar por nome
 * - Buscar por ID
 * - Remover planeta
 */
@RestController
@RequestMapping("/api/planetas")
public class PlanetaController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PlanetaController.class);
    private final PlanetaService planetaService;

    public PlanetaController(PlanetaService planetaService) {
        this.planetaService = planetaService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> adicionar(@RequestBody Planeta planeta) throws Exception {
        var novoPlaneta = planetaService.adicionar(planeta);
        if (novoPlaneta == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        URI uri201 = URI.create(String.format("/api/planetas/?nome=%s", novoPlaneta.getNome()));
        return ResponseEntity.created(uri201).build();
    }

    /**
     * Lista todos os planetas ou busca um planeta através dos parâmetros especificados em PlanetaQuerystring
     * Exemplos: 
     *  - http://localhost:8080/api/planetas
     *  - http://localhost:8080/api/planetas/?id=1
     *  - http://localhost:8080/api/planetas/?nome=tatooine
     * 
     * @param querystring parâmetros fornecidos para busca de um planeta específico
     * @return
     * @throws Exception
     */
    @GetMapping()
    public ResponseEntity<?> listar(@Valid PlanetaQueryString querystring) throws Exception {
        
        if (querystring.getId() != 0) {
            var planetaExistente = planetaService.buscarPorId(querystring.getId());
            if (planetaExistente == null)
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok(planetaExistente);
        }

        if (querystring.getNome() != null) {
            var planetaExistente = planetaService.buscarPorNome(querystring.getNome());
            if (planetaExistente == null)
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok(planetaExistente);
        }

        var planetasExistentes = planetaService.listarTodos();    
        return ResponseEntity.ok(planetasExistentes);
    }

    @DeleteMapping("{nome}")
    public ResponseEntity<?> excluir(@PathVariable String nome) {
        planetaService.excluir(nome);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
        LOGGER.error("Erro inesperado.", e);
        return e;
    }
}
