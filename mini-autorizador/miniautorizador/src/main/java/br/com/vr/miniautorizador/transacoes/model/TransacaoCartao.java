package br.com.vr.miniautorizador.transacoes.model;

import lombok.Getter;

public class TransacaoCartao {

	@Getter
	private String numeroCartao;

	@Getter
	private String senhaCartao;

	@Getter
	double valorTransacao;

	public TransacaoCartao(String numeroCartao, String senhaCartao, double valor) {
		this.numeroCartao = numeroCartao;
		this.senhaCartao = senhaCartao;
		this.valorTransacao = valor;
	}
}
