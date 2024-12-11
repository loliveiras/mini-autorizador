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
	
	/*
	 * NESTE METODO FOI ADICIONADO UMA CODIFICACAO DE SENHA DO CARTAO, PARA QUE A MESMA NAO FIQUE EXPOSTA.
	 */
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
	/*
	 * ESTE METODO FOI DESENVOLVIDO PARA REALIZAR A AUTORIZACAO DE UMA TRANSACAO DE CARTAO DE CREDITO.
	 * FOI UTILIZADO UMA ABORDAGEM DE ATUALIZACAO ATOMICA QUE GARANTE A CONSISTENCIA DOS DADOS
	 * E A SEGURANCA DA OPERACAO.
	 */
	public void autorizarTransacao(TransacaoCartao transacao) throws CartoesException {
		
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