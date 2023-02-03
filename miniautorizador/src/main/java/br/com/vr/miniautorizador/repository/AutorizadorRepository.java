package br.com.vr.miniautorizador.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.vr.miniautorizador.model.Cartao;

@Repository
public interface AutorizadorRepository extends MongoRepository<Cartao, String> {

}
