package br.com.vr.miniautorizador.cartoes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.NonNull;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "GERENCIAMENTO-CARTOES")
public class Cartao {
	
	@Id
	@NonNull
	@Getter
	private String numeroCartao;

	@NonNull
	@Getter
	@Setter
	private String senha;
	
	@JsonIgnore
	@Getter
	@Setter
	private double saldo;

	public Cartao(String numeroCartao, String senha) {
		this.numeroCartao = numeroCartao;
		this.senha = senha;
	}
}