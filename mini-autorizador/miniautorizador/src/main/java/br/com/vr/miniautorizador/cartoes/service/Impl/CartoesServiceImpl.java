package br.com.vr.miniautorizador.cartoes.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.vr.miniautorizador.cartoes.model.Cartoes;
import br.com.vr.miniautorizador.cartoes.model.TransacaoCartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;
import br.com.vr.miniautorizador.cartoes.service.CartoesService;
import br.com.vr.miniautorizador.cartoes.validation.CartoesValidator;
import br.com.vr.miniautorizador.exception.CartoesException;

@Service
public class CartoesServiceImpl implements CartoesService {
	
	@Autowired
	private CartoesRepository cartoesRepository;
	
	@Autowired
	private CartoesValidator cartaoValidator;
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public Cartoes registrarCartao(Cartoes cartao) throws CartoesException {
		cartaoValidator.validarCartaoExistente(cartao, cartoesRepository);
		String senhaCodificada = passwordEncoder.encode(cartao.getSenha());
		cartao.setSenha(senhaCodificada);
		cartao.setSaldo(500.0);
		cartoesRepository.save(cartao);
		return cartao;
	}
	public double obterSaldoCartao(String numeroCartao) throws CartoesException {
		cartaoValidator.validarCartaoInexistente(numeroCartao, cartoesRepository);
		return cartoesRepository.findById(numeroCartao).get().getSaldo();
	}
	
	public void processarTransacao(TransacaoCartao transacao) throws CartoesException {
		
		cartaoValidator.validarTransacaoCartao(transacao, cartoesRepository);
        
		Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(transacao.getNumeroCartao())
                                  .and("saldo").gte(transacao.getValorTransacao()));
        
        Update update = new Update();
        update.inc("saldo", -transacao.getValorTransacao());
        
        Cartoes cartao = mongoTemplate.findAndModify(query, update, Cartoes.class);
        cartaoValidator.validarSaldoDisponivelCartao(cartao);
	}
}