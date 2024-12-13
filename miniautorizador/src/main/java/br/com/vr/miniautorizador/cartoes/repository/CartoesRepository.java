package br.com.vr.miniautorizador.cartoes.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.vr.miniautorizador.cartoes.model.Cartoes;

@Repository
public interface CartoesRepository extends MongoRepository<Cartoes, String> {
	
	@Transactional
    @Query("{ '_id': ?0, 'saldo': { '$gte': ?1 } }")
    @Update("{ '$inc': { 'saldo': -?1 } }")
    int atualizarSaldo(String numeroCartao, double valorTransacao);
}
