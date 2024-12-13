package br.com.vr.miniautorizador.cartoes.repository;

import br.com.vr.miniautorizador.cartoes.model.Cartoes;

public interface CartaoRepositoryCustom {
	Cartoes atualizarSaldo(String numeroCartao, double valorTransacao);
}
