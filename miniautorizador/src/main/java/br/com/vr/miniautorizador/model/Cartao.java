package br.com.vr.miniautorizador.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "miniautorizador")
public class Cartao {
	
	private Double saldo;
	private String senha;
	private String numeroCartao;
	
	public Cartao(String senha, String numeroCartao) {
		saldo = 500.0;
		this.senha = senha;
		this.numeroCartao = numeroCartao;
	}
}
