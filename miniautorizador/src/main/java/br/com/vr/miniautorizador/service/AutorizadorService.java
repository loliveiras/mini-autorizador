package br.com.vr.miniautorizador.service;

import org.springframework.stereotype.Service;

import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.repository.AutorizadorRepository;

@Service
public class AutorizadorService {
	
	private AutorizadorRepository autorizadorRepository;

	public Cartao save(Cartao cartao) {
		return autorizadorRepository.save(cartao);
	}

	public Cartao find(String numeroCartao) throws Exception {
		return autorizadorRepository.findById(numeroCartao)
				.orElseThrow(() -> new Exception("O cartão: " + numeroCartao + " não existe."));
	}
}
