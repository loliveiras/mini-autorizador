package br.com.vr.miniautorizador.cartoes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import br.com.vr.miniautorizador.enums.MiniAutorizadorEnum;
import br.com.vr.miniautorizador.transacoes.model.Transacao;
import lombok.Getter;

@Document
public class Cartao {

	@NonNull
	@Getter
	private String senha;
	
	@Id
	@NonNull
	@Getter
	private String numeroCartao;
	
	@Getter
	private double saldo;
	
	@Version
	private int controleTransacao;

	public Cartao(String senha, String numeroCartao) {
		saldo = 500.0;
		this.senha = senha;
		this.numeroCartao = numeroCartao;
	}
	
	public void atualizaSaldo(Cartao cartao, double valor) {
		cartao.saldo -= valor;
	}
	
	public void verificaSaldo(Transacao transacao, Cartao cartao) {
		if(!(cartao.getSaldo() >= transacao.getValor())) {
			throw new RuntimeException(MiniAutorizadorEnum.SALDO_INSUFICIENTE.name());
		}
	}
	
	public void verificaSenha(Transacao transacao, Cartao cartao) {
		if(!transacao.getSenhaCartao().equals(cartao.getSenha())) {
			throw new IllegalArgumentException(MiniAutorizadorEnum.SENHA_INVALIDA.name());
 		}
	}
}