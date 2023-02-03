package br.com.vr.miniautorizador.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.repository.AutorizadorRepository;

@Service
public class AutorizadorService {
	
	@Autowired
	private AutorizadorRepository autorizadorRepository;

	public Cartao save(Cartao cartao) {
		return autorizadorRepository.save(cartao);
	}
	
	public void verificarSeCartaoExiste(String numeroCartao) throws Exception {
		Optional<Cartao> cartaoExiste = autorizadorRepository.findById(numeroCartao);
		
		if(null != cartaoExiste)
			throw new Exception("Cartão já existe!");
	}
	
	public Optional<Cartao> find(String numeroCartao) throws Exception {
		return autorizadorRepository.findById(numeroCartao);
	}
}
