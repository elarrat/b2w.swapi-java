package br.com.b2w.repositories.impl;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import br.com.b2w.models.Planeta;
import br.com.b2w.repositories.PlanetaRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PlanetaRepositoryMongoDB implements PlanetaRepository {

    @Autowired
    private MongoClient mongoClient;
    private MongoCollection<Planeta> planetas;

    @PostConstruct
    void init() {
        planetas = mongoClient.getDatabase("desafio-b2w").getCollection("planetas", Planeta.class);
    }

    @Override
    public Planeta salvar(Planeta planeta) {
        planetas.insertOne(planeta);
        return planeta;
    }

    @Override
    public List<Planeta> listarTodos() {
        return planetas.find().into(new ArrayList<>());
    }

    @Override
    public Planeta buscarPorNome(String nome) {
        return planetas.find(eq("nome", nome)).first();
    }

    @Override
    public long excluir(String nome) {
        return planetas.deleteOne(eq("nome", nome)).getDeletedCount();
    }

    @Override
    public long excluirTodos() {
        return planetas.deleteMany(new BsonDocument()).getDeletedCount();
    }

    @Override
    public long contarTodos() {
        return planetas.countDocuments();
    }
}