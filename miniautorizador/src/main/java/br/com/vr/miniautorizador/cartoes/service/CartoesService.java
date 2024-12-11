package br.com.vr.miniautorizador.cartoes.service;

import br.com.vr.miniautorizador.cartoes.model.Cartoes;
import br.com.vr.miniautorizador.cartoes.model.TransacaoCartao;
import br.com.vr.miniautorizador.exception.CartoesException;

public interface CartoesService {

	public Cartoes registrarCartao(Cartoes cartao) throws CartoesException;
	public double obterSaldoCartao(String numeroCartao) throws CartoesException;
	public void autorizarTransacao(TransacaoCartao transacao) throws CartoesException;
}
