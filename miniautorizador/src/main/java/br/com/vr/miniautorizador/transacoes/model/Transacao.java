package br.com.vr.miniautorizador.transacoes.model;

import lombok.Getter;

public class Transacao {
	
	@Getter
	private String numeroCartao;
	
	@Getter
	private String senhaCartao;
	
	@Getter
	double valor;
}
