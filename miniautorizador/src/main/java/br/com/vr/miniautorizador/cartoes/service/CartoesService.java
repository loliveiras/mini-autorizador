package br.com.vr.miniautorizador.cartoes.service;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.transacoes.model.Transacao;

public interface CartoesService {
	
	public Cartao criar(Cartao cartao);
	public Cartao obterSaldo(String numeroCartao);
	public void transacao(Transacao transacao);
}
