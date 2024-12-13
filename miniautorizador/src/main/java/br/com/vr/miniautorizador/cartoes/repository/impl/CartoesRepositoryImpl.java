package br.com.vr.miniautorizador.cartoes.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.vr.miniautorizador.cartoes.model.Cartoes;
import br.com.vr.miniautorizador.cartoes.repository.CartaoRepositoryCustom;

@Repository
public class CartoesRepositoryImpl implements CartaoRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Transactional
    public Cartoes atualizarSaldo(String numeroCartao, double valorTransacao) {
        Query query = new Query(Criteria.where("_id").is(numeroCartao).and("saldo").gte(valorTransacao));
        
        Update update = new Update().inc("saldo", -valorTransacao);
        
        return mongoTemplate.findAndModify(query, update, Cartoes.class);
		
    }
}
