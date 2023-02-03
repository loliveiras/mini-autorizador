package br.com.vr.miniautorizador.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "cartao")
public class Cartao {

	@NonNull
	private String senha;
	
	@Id
	@NonNull
	private String numeroCartao;

	private Double saldo;

	public Cartao(String senha, String numeroCartao) {
		saldo = 500.0;
		this.senha = senha;
		this.numeroCartao = numeroCartao;
	}
	
	public String getSenha() {
		return senha;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}
}