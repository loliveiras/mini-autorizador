package br.com.vr.miniautorizador.service;

import org.springframework.stereotype.Service;

import br.com.vr.miniautorizador.model.CartaoModel;

@Service
public class AutorizadorService {
	
	public CartaoModel save(CartaoModel cartaoModel) {
		return cartaoModel;
	}

	public CartaoModel find(String numeroCartao) {
		return null;
	}
}
