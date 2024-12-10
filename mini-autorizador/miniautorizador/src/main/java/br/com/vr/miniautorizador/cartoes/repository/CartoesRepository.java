package br.com.vr.miniautorizador.cartoes.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.vr.miniautorizador.cartoes.model.Cartao;

@Repository
public interface CartoesRepository extends MongoRepository<Cartao, String> {
}
