package br.com.b2w.services;

import br.com.b2w.models.swapi.SwapiCollection;
import br.com.b2w.models.swapi.SwapiPlanetResult;

import org.springframework.stereotype.Service;

@Service
public interface SwapiService {

    /**
     * Busca planetas na SWAPI que se assemelham ao nome desejado
     * 
     * @param nome nome do planeta a ser buscado
     * @return
     * @throws Exception
     */
    SwapiCollection<SwapiPlanetResult> buscarPlanetaPorNome(String nome) throws Exception;

    /**
     * Busca um planeta na SWAPI através do seu id associado
     * 
     * @param id id do planeta a ser buscado
     * @return
     * @throws Exception
     */
    SwapiPlanetResult buscarPlanetaPorId(int id) throws Exception;
}
