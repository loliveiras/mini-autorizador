package br.com.vr.miniautorizador.transacoes;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;
import br.com.vr.miniautorizador.enums.MiniAutorizadorEnum;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.model.TransacaoCartao;

@Component
public class ValidarCartao {
	
	/*
	 * 
	 * NESTA CLASSE EU PODERIA REUTILIZAR CÓDIGO, MAS DECIDI SEPARAR A RESPONSABILIDADE DE CADA VALIDAÇÃO
	 * DESSA FORMA TEMOS UMA CLAREZA MAIS EFICAZ E POSTERIORMENTE CASO PRECISE DE MANUTENÇÃO FICA MAIS SIMPLES, ALÉM DA
	 * ABORDAGEM DO PRINCÍPIO DA RESPONSÁBILIDADE ÚNICA.
	 * 
	*/
	@Autowired
	PasswordEncoder passwordEncode;	
	
	public boolean validarCartaoExistente(Cartao cartao, CartoesRepository cartoesRepository) throws MiniAutorizadorException {
		 return Optional.ofNullable(cartao)
                 .map(c -> cartoesRepository.existsById(c.getNumeroCartao()))
                 .filter(existe -> !existe)
                 .orElseThrow(() -> new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_JA_EXISTE.name()));
	}
	public boolean validarCartaoInexistente(String numeroCartao, CartoesRepository cartoesRepository) throws MiniAutorizadorException {
		 return Optional.ofNullable(numeroCartao)
                .map(c -> cartoesRepository.existsById(numeroCartao))
                .filter(existe -> existe)
                .orElseThrow(() -> new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name()));
	}
	public void validaTransacaoCartao(TransacaoCartao transacao, CartoesRepository cartoesRepository) throws MiniAutorizadorException {
		validarCartaoInexistente(transacao.getNumeroCartao(), cartoesRepository);
		validarSenhaCartao(transacao, cartoesRepository);
	}
	public void validarSaldoDisponivelCartao(Cartao cartao) throws MiniAutorizadorException {
		Optional.ofNullable(cartao)
        .orElseThrow(() -> new MiniAutorizadorException(MiniAutorizadorEnum.SALDO_INSUFICIENTE.name()));
		
	}
	private void validarSenhaCartao(TransacaoCartao transacao, CartoesRepository cartoesRepository) throws MiniAutorizadorException {
		Cartao cartao = cartoesRepository.findById(transacao.getNumeroCartao())
	            .orElseThrow(() -> new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name()));
		
		boolean senhaValida = passwordEncode.matches(transacao.getSenhaCartao(), cartao.getSenha());
	    Optional.of(senhaValida)
	            .filter(valida -> valida)
	            .orElseThrow(() -> new MiniAutorizadorException(MiniAutorizadorEnum.SENHA_INVALIDA.name()));
	}
}