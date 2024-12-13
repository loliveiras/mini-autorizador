package br.com.vr.miniautorizador.cartoes.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.vr.miniautorizador.cartoes.model.Cartoes;

@Repository
public interface CartoesRepository extends MongoRepository<Cartoes, String>, CartaoRepositoryCustom {
}
