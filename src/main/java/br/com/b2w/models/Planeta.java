package br.com.b2w.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Planeta {

    private String nome;
    
    private String clima;
    
    private String terreno;

    private transient int aparicoes;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getClima() {
        return this.clima;
    }

    public void setTerreno(String terreno) {
        this.terreno = terreno;
    }

    public String getTerreno() {
        return this.terreno;
    }

    public void setAparicoes(int aparicoes) {
        this.aparicoes = aparicoes;
    }

    public int getAparicoes() {
        return this.aparicoes;
    }

    @Override
    public String toString() {
        return "Planeta{nome='" + nome + '\'' + ", clima='" + clima + '\'' + ", terreno=" + terreno + ", aparicoes=" + aparicoes + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;
        
        Planeta planeta = (Planeta) o;
        return nome.equalsIgnoreCase(planeta.getNome()) 
            && clima.equalsIgnoreCase(planeta.getClima())
            && terreno.equalsIgnoreCase(planeta.getTerreno());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, clima, terreno, aparicoes);
    }
}