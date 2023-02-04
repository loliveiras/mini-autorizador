package br.com.vr.miniautorizador.service;

import br.com.vr.miniautorizador.model.Cartao;

public interface CartoesService {
	
	public Cartao criar(Cartao cartao);
	public Cartao obterSaldo(String numeroCartao);
	public boolean exiteCartao(String numeroCartao);
}
