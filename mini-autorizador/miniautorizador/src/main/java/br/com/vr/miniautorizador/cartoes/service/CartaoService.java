package br.com.vr.miniautorizador.cartoes.service;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.model.TransacaoCartao;

public interface CartaoService {

	public Cartao registrarCartao(Cartao cartao) throws MiniAutorizadorException;
	public double obterSaldoCartao(String numeroCartao) throws MiniAutorizadorException;
	public void processarTransacao(TransacaoCartao transacao) throws MiniAutorizadorException;
}
