package br.com.b2w.models.querystring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Abstrai os parâmetros de busca de um planeta passados via querystring
 */
@JsonInclude(Include.NON_NULL)
public class PlanetaQueryString {

    private int id;

    private String nome;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}