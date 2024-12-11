package br.com.vr.miniautorizador.cartoes.model;

import lombok.Getter;
import lombok.Setter;

public class TransacaoCartao {

	@Getter
	private String numeroCartao;

	@Getter
	@Setter
	private String senhaCartao;

	@Getter
	double valorTransacao;

	public TransacaoCartao(String numeroCartao, String senhaCartao, double valor) {
		this.numeroCartao = numeroCartao;
		this.senhaCartao = senhaCartao;
		this.valorTransacao = valor;
	}
}
