package br.com.b2w.services;

import java.util.List;

import br.com.b2w.models.Planeta;

import org.springframework.stereotype.Service;

@Service
public interface PlanetaService {

    /**
     * Persiste um novo planeta na base local
     * 
     * @param novoPlaneta
     * @return
     * @throws Exception
     */
    Planeta adicionar(Planeta novoPlaneta) throws Exception;

    /**
     * Lista todos os planetas persistidos na base local
     * 
     * @return
     * @throws Exception
     */
    List<Planeta> listarTodos() throws Exception;

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
    Planeta buscarPorNome(String nome, boolean buscaLocal) throws Exception;

    /**
     * Busca um planeta com o nome especificado apenas na base local.
     * 
     * @param nome o nome a ser buscado na base de dados local
     * @return
     * @throws Exception
     */
    Planeta buscarPorNome(String nome) throws Exception;

    /**
     * Busca informações de um planeta através do seu id associado na SWAPI e
     * retorna um objeto Planeta conforme o modelo de dados do projeto.
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Planeta buscarPorId(int id) throws Exception;

    /**
     * Exclui um planeta da base de dados local
     * 
     * @param nome nome do planeta a ser excluído
     */
    void excluir(String nome);
}
