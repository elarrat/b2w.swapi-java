package br.com.b2w.services.impl;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import br.com.b2w.models.swapi.SwapiCollection;
import br.com.b2w.models.swapi.SwapiPlanetResult;
import br.com.b2w.services.SwapiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Acesso aos endpoints da SWAPI
 */
@Service
public class SwapiServiceImpl implements SwapiService {

    @Value("${swapi.base.url}")
    private String URL_BASE;

    /**
     * Busca planetas na SWAPI que se assemelham ao nome desejado
     * 
     * @param nome nome do planeta a ser buscado
     * @return
     * @throws Exception
     */
    public SwapiCollection<SwapiPlanetResult> buscarPlanetaPorNome(String nome) throws Exception {
        try {
            var endpoint = URI.create(URL_BASE + "planets/?search=" + nome);
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(endpoint).build();

            ObjectMapper mapper = new ObjectMapper();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());

            var swapiCollection = mapper.readValue(httpResponse.body(),
                    new TypeReference<SwapiCollection<SwapiPlanetResult>>() {
                    });
            return swapiCollection;
        } catch (JsonMappingException jex) {
            throw new Exception("Erro de mapeamento de Json na leitura da resposta do SWAPI.");
        } catch (ConnectException cex) {
            throw new Exception("Erro de comunicação com a SWAPI.");
        }
    }

    /**
     * Busca um planeta na SWAPI através do seu id associado
     * 
     * @param id id do planeta a ser buscado
     * @return
     * @throws Exception
     */
    public SwapiPlanetResult buscarPlanetaPorId(int id) throws Exception {
        try {
            var endpoint = URI.create(URL_BASE + "planets/" + id + "/");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(endpoint).build();

            ObjectMapper mapper = new ObjectMapper();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());
            if (httpResponse.statusCode() == 404)
                return null;

            var swapiResult = mapper.readValue(httpResponse.body(), SwapiPlanetResult.class);
            return swapiResult;
        } catch (JsonMappingException jex) {
            throw new Exception("Erro de mapeamento de Json na leitura da resposta do SWAPI.");
        } catch (ConnectException cex) {
            throw new Exception("Erro de comunicação com a SWAPI.");
        }
    }
}