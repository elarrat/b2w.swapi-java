package br.com.b2w.repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

import br.com.b2w.models.Planeta;

@Repository
public interface PlanetaRepository {

    Planeta salvar(Planeta planeta);

    List<Planeta> listarTodos();

    Planeta buscarPorNome(String nome);

    long excluir(String id);

    long excluirTodos();

    long contarTodos();

}
