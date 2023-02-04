package br.com.vr.miniautorizador.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.repository.CartoesRepository;
import br.com.vr.miniautorizador.service.CartoesService;

@Service
public class CartoesServiceImpl implements CartoesService  {
	
	@Autowired
	private CartoesRepository autorizadorRepository;

	@Override
	public Cartao criar(Cartao cartao) {
		return this.autorizadorRepository.save(cartao);
	}

	@Override
	public Cartao obterSaldo(String numeroCartao) {
		return  this.autorizadorRepository
				.findById(numeroCartao)
				.orElseThrow(() -> new IllegalArgumentException("Cartão não existe!"));
	}

	@Override
	public boolean exiteCartao(String numeroCartao) {
		return this.autorizadorRepository.existsById(numeroCartao);
	}
}
