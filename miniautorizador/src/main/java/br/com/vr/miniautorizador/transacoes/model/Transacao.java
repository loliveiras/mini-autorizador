package br.com.vr.miniautorizador.transacoes.model;

import lombok.Getter;

public class Transacao {

	@Getter
	private String numeroCartao;

	@Getter
	private String senhaCartao;

	@Getter
	double valor;

	public Transacao(String numeroCartao, String senhaCartao, double valor) {
		this.numeroCartao = numeroCartao;
		this.senhaCartao = senhaCartao;
		this.valor = valor;
	}
}
