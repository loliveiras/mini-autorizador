package br.com.vr.miniautorizador.cartoes.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public Cartoes registrarCartao(Cartoes cartao) throws CartoesException {
		cartaoValidator.validarCartaoExistente(cartao, cartoesRepository);
		cartao.setSaldo(500.0);
		cartoesRepository.save(cartao);
		return cartao;
	}
	public double obterSaldoCartao(String numeroCartao) throws CartoesException {
		cartaoValidator.validarCartaoInexistente(numeroCartao, cartoesRepository);
		return cartoesRepository.findById(numeroCartao).get().getSaldo();
	}
	/*
	 * ESTE METODO FOI DESENVOLVIDO PARA REALIZAR A AUTORIZACAO DE UMA TRANSACAO DE CARTAO.
	 * FOI UTILIZADO UMA ABORDAGEM DE ATUALIZACAO ATOMICA QUE GARANTE A CONSISTENCIA DOS DADOS
	 * E A SEGURANCA DA OPERACAO.
	 */
	public void autorizarTransacao(TransacaoCartao transacao) throws CartoesException {
		cartaoValidator.validarTransacaoCartao(transacao, cartoesRepository);
        
		 int updatedCount = cartoesRepository.atualizarSaldo(transacao.getNumeroCartao(), transacao.getValorTransacao());
		 if (updatedCount == 0) {
			 throw new CartoesException("Saldo insuficiente ou cartão não encontrado.");
		 }
		 Cartoes cartao = cartoesRepository.findById(transacao.getNumeroCartao()).orElseThrow(() -> new CartoesException("Cartão não encontrado após atualização."));
		 cartaoValidator.validarSaldoDisponivelCartao(cartao);
	}
}