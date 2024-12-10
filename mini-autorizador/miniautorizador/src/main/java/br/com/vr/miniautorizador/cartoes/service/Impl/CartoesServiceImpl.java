package br.com.vr.miniautorizador.cartoes.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;
import br.com.vr.miniautorizador.cartoes.service.CartaoService;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.ValidarCartao;
import br.com.vr.miniautorizador.transacoes.model.TransacaoCartao;

@Service
public class CartoesServiceImpl implements CartaoService {
	
	@Autowired
	private CartoesRepository cartoesRepository;
	
	@Autowired
	private ValidarCartao validarCartao;
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public Cartao registrarCartao(Cartao cartao) throws MiniAutorizadorException {
		validarCartao.validarCartaoExistente(cartao, cartoesRepository);
		String senhaCodificada = passwordEncoder.encode(cartao.getSenha());
		cartao.setSenha(senhaCodificada);
		cartao.setSaldo(500.0);
		cartoesRepository.save(cartao);
		return cartao;
	}
	public double obterSaldoCartao(String numeroCartao) throws MiniAutorizadorException {
		validarCartao.validarCartaoInexistente(numeroCartao, cartoesRepository);
		return cartoesRepository.findById(numeroCartao).get().getSaldo();
	}
	
	public void processarTransacao(TransacaoCartao transacao) throws MiniAutorizadorException {
		
		validarCartao.validaTransacaoCartao(transacao, cartoesRepository);
        
		Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(transacao.getNumeroCartao())
                                  .and("saldo").gte(transacao.getValorTransacao()));
        
        Update update = new Update();
        update.inc("saldo", -transacao.getValorTransacao());
        
        Cartao cartao = mongoTemplate.findAndModify(query, update, Cartao.class);
        validarCartao.validarSaldoDisponivelCartao(cartao);
	}
}