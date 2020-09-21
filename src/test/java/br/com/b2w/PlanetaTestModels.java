package br.com.b2w;

import br.com.b2w.models.Planeta;

import org.springframework.stereotype.Component;

@Component
class PlanetaTestModels {

    Planeta getTatooine() {
        var planeta = new Planeta();
        planeta.setNome("Tatooine");
        planeta.setClima("arid");
        planeta.setTerreno("desert");

        return planeta;
    }

    Planeta getAlderaan() {
        var planeta = new Planeta();
        planeta.setNome("Alderaan");
        planeta.setClima("Temperado");
        planeta.setTerreno("Florestal");

        return planeta;
    }

    Planeta getHoth() {
        var planeta = new Planeta();
        planeta.setNome("Hoth");
        planeta.setClima("Gelado");
        planeta.setTerreno("Cavernoso");

        return planeta;
    }
}
