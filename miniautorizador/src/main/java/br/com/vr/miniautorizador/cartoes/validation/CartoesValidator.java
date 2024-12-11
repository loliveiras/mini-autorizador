package br.com.vr.miniautorizador.cartoes.validation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.vr.miniautorizador.cartoes.model.Cartoes;
import br.com.vr.miniautorizador.cartoes.model.TransacaoCartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;
import br.com.vr.miniautorizador.enums.CartoesEnum;
import br.com.vr.miniautorizador.exception.CartoesException;

@Component
public class CartoesValidator {
	
	/*
	 * 
	 * NESTA CLASSE EU PODERIA REUTILIZAR CÓDIGO, MAS DECIDI SEPARAR A RESPONSABILIDADE DE CADA VALIDAÇÃO
	 * DESSA FORMA TEMOS UMA CLAREZA MAIS EFICAZ E POSTERIORMENTE CASO PRECISE DE MANUTENÇÃO FICA MAIS SIMPLES, ALÉM DA
	 * ABORDAGEM DO PRINCÍPIO DA RESPONSÁBILIDADE ÚNICA.
	 * 
	*/
	@Autowired
	PasswordEncoder passwordEncode;	
	
	public boolean validarCartaoExistente(Cartoes cartao, CartoesRepository cartoesRepository) throws CartoesException {
		 return Optional.ofNullable(cartao)
                 .map(c -> cartoesRepository.existsById(c.getNumeroCartao()))
                 .filter(existe -> !existe)
                 .orElseThrow(() -> new CartoesException(CartoesEnum.CARTAO_JA_REGISTRADO.name()));
	}
	public boolean validarCartaoInexistente(String numeroCartao, CartoesRepository cartoesRepository) throws CartoesException {
		 return Optional.ofNullable(numeroCartao)
                .map(c -> cartoesRepository.existsById(numeroCartao))
                .filter(existe -> existe)
                .orElseThrow(() -> new CartoesException(CartoesEnum.CARTAO_INEXISTENTE.name()));
	}
	public void validarTransacaoCartao(TransacaoCartao transacao, CartoesRepository cartoesRepository) throws CartoesException {
		validarCartaoInexistente(transacao.getNumeroCartao(), cartoesRepository);
		validarSenhaCartao(transacao, cartoesRepository);
	}
	public void validarSaldoDisponivelCartao(Cartoes cartao) throws CartoesException {
		Optional.ofNullable(cartao)
        .orElseThrow(() -> new CartoesException(CartoesEnum.SALDO_INSUFICIENTE.name()));
		
	}
	public void validarSenhaCartao(TransacaoCartao transacao, CartoesRepository cartoesRepository) throws CartoesException {
		Cartoes cartao = cartoesRepository.findById(transacao.getNumeroCartao())
	            .orElseThrow(() -> new CartoesException(CartoesEnum.CARTAO_INEXISTENTE.name()));
		
		boolean senhaValida = passwordEncode.matches(transacao.getSenhaCartao(), cartao.getSenha());
	    Optional.of(senhaValida)
	            .filter(valida -> valida)
	            .orElseThrow(() -> new CartoesException(CartoesEnum.SENHA_INVALIDA.name()));
	}
}