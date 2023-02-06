package br.com.vr.miniautorizador.cartoes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import br.com.vr.miniautorizador.enums.MiniAutorizadorEnum;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.model.Transacao;
import lombok.Getter;

@Document
public class Cartao {
	
	@Id
	@NonNull
	@Getter
	private String numeroCartao;

	@NonNull
	@Getter
	private String senha;

	@Getter
	private double saldo;

	@Version
	private int controleTransacao;

	public Cartao(String numeroCartao, String senha) {
		saldo = 500.0;
		this.numeroCartao = numeroCartao;
		this.senha = senha;
	}

	public void atualizaSaldo(Cartao cartao, double valor) {
		cartao.saldo -= valor;
	}

	public void verificaSaldo(Transacao transacao, Cartao cartao) throws MiniAutorizadorException {
		if(!(cartao.getSaldo() >= transacao.getValor())) {
			throw new MiniAutorizadorException(MiniAutorizadorEnum.SALDO_INSUFICIENTE.name());
		}
	}

	public void verificaSenha(Transacao transacao, Cartao cartao) throws MiniAutorizadorException {
		if(!transacao.getSenhaCartao().equals(cartao.getSenha())) {
			throw new MiniAutorizadorException(MiniAutorizadorEnum.SENHA_INVALIDA.name());
 		}
	}
}