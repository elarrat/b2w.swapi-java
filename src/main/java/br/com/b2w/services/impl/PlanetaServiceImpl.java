package br.com.b2w.services.impl;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import br.com.b2w.models.Planeta;
import br.com.b2w.repositories.PlanetaRepository;
import br.com.b2w.services.PlanetaService;
import br.com.b2w.services.SwapiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Manipulação dos planetas na base local
 */
@Service
public class PlanetaServiceImpl implements PlanetaService {

    @Autowired
    private SwapiService swapiService;

    @Autowired
    private PlanetaRepository planetaRepo;

    /**
     * Persiste um novo planeta na base local
     * 
     * @param novoPlaneta
     * @return
     * @throws Exception
     */
    public Planeta adicionar(Planeta novoPlaneta) throws Exception {
        var planetaExistente = buscarPorNome(novoPlaneta.getNome(), true);
        if (planetaExistente != null)
            return null;

        return planetaRepo.salvar(novoPlaneta);
    }

    /**
     * Lista todos os planetas persistidos na base local
     * 
     * @return
     * @throws Exception
     */
    public List<Planeta> listarTodos() throws Exception {
        var planetasExistentes = planetaRepo.listarTodos();

        preenchendoPlanetas: for (Planeta planetaExistente : planetasExistentes) {

            String nomeEncoded = URLEncoder.encode(planetaExistente.getNome(), StandardCharsets.UTF_8);
            var swapiCollection = swapiService.buscarPlanetaPorNome(nomeEncoded);
            var swapiPlanetResult = swapiCollection.getResults();

            if (swapiPlanetResult != null && swapiPlanetResult.length > 0) {

                /**
                 * A SWAPI não aprofunda muito o assunto de busca, então talvez possa acontecer
                 * colisões de nomes semelhantes que estejam contindo em outros nomes. Exemplos
                 * hipotéticos: - Naboo / Nabolisk - Tatoo / Tatooine Para esses casos, ao
                 * buscar pelo primeiro exemplo, os dois registros retornarão, e como a API não
                 * garante uma ordem dos registros na busca, existe a confirmação do nome
                 * retornado com o nome buscado
                 */
                for (int i = 0; i < swapiPlanetResult.length; i++) {
                    var planetaSWAPI = swapiPlanetResult[i];
                    if (planetaExistente.getNome().equalsIgnoreCase(planetaSWAPI.getName())) {
                        planetaExistente.setAparicoes(planetaSWAPI.getFilms().length);
                        continue preenchendoPlanetas;
                    }
                }
            }
        }

        return planetasExistentes;
    }

    /**
     * Busca um planeta com o nome especificado no parâmetro na base local e lista
     * informações associadas a ele na SWAPI. O Parâmetro buscaLocal desabilita a
     * busca das informações adicionais na SWAPI
     * 
     * @param nome       o nome a ser buscado
     * @param buscaLocal quando habilitado, busca o planeta apenas na base local
     * @return
     * @throws Exception
     */
    public Planeta buscarPorNome(String nome, boolean buscaLocal) throws Exception {
        Planeta planetaExistente = planetaRepo.buscarPorNome(nome);
        if (planetaExistente == null)
            return null;

        if (buscaLocal)
            return planetaExistente;

        String nomeEncoded = URLEncoder.encode(planetaExistente.getNome(), StandardCharsets.UTF_8);
        var swapiCollection = swapiService.buscarPlanetaPorNome(nomeEncoded);
        var swapiPlanetResult = swapiCollection.getResults();
        if (swapiPlanetResult != null && swapiPlanetResult.length > 0) {

            /**
             * A SWAPI não aprofunda muito o assunto de busca, então talvez possa acontecer
             * colisões de nomes semelhantes que estejam contindo em outros nomes. Exemplos
             * hipotéticos: - Naboo / Nabolisk - Tatoo / Tatooine Para esses casos, ao
             * buscar pelo primeiro exemplo, os dois registros retornarão, e como a API não
             * garante uma ordem dos registros na busca, existe a confirmação do nome
             * retornado com o nome buscado
             */
            for (int i = 0; i < swapiPlanetResult.length; i++) {
                var planetaSWAPI = swapiPlanetResult[i];
                if (planetaExistente.getNome().equalsIgnoreCase(planetaSWAPI.getName())) {
                    planetaExistente.setAparicoes(planetaSWAPI.getFilms().length);
                    break;
                }
            }
        }

        return planetaExistente;
    }

    /**
     * Busca um planeta com o nome especificado apenas na base local.
     * 
     * @param nome o nome a ser buscado na base de dados local
     * @return
     * @throws Exception
     */
    public Planeta buscarPorNome(String nome) throws Exception {
        return buscarPorNome(nome, false);
    }

    /**
     * Busca informações de um planeta através do seu id associado na SWAPI e
     * retorna um objeto Planeta conforme o modelo de dados do projeto.
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public Planeta buscarPorId(int id) throws Exception {
        var swapiPlanetResult = swapiService.buscarPlanetaPorId(id);
        if (swapiPlanetResult == null)
            return null;

        var planeta = new Planeta();
        planeta.setNome(swapiPlanetResult.getName());
        planeta.setClima(swapiPlanetResult.getClimate());
        planeta.setTerreno(swapiPlanetResult.getTerrain());
        planeta.setAparicoes(swapiPlanetResult.getFilms().length);

        return planeta;
    }

    /**
     * Exclui um planeta da base de dados local
     * 
     * @param nome nome do planeta a ser excluído
     */
    public void excluir(String nome) {
        String nomeDecoded = URLDecoder.decode(nome, StandardCharsets.UTF_8);
        planetaRepo.excluir(nomeDecoded);
    }
}