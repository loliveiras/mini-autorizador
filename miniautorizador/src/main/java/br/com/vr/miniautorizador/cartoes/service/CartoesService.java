package br.com.vr.miniautorizador.cartoes.service;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.model.Transacao;

public interface CartoesService {
	
	public Cartao criarCartao(Cartao cartao) throws MiniAutorizadorException;
	public Cartao obterSaldo(String numeroCartao) throws MiniAutorizadorException;
	public void transacao(Transacao transacao) throws MiniAutorizadorException;
}
