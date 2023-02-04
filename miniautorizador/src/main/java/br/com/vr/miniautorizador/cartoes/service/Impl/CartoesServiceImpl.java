package br.com.vr.miniautorizador.cartoes.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;
import br.com.vr.miniautorizador.cartoes.service.CartoesService;
import br.com.vr.miniautorizador.enums.MiniAutorizadorEnum;
import br.com.vr.miniautorizador.transacoes.model.Transacao;

@Service
public class CartoesServiceImpl implements CartoesService  {
	
	@Autowired
	private CartoesRepository cartoesRepository;
	
	@Override
	public Cartao criar(Cartao cartao) {
		
		boolean existe = cartoesRepository.existsById(cartao.getNumeroCartao());

		if (existe)
			throw new IllegalArgumentException();
		return this.cartoesRepository.save(cartao);
	}

	@Override
	public Cartao obterSaldo(String numeroCartao) {
		return  this.cartoesRepository
				.findById(numeroCartao)
				.orElseThrow(() -> new IllegalArgumentException(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name()));
	}

	@Override
	public void transacao(Transacao transacao) {
		
		Cartao cartao = cartoesRepository
				.findById(transacao.getNumeroCartao())
				.orElseThrow(() -> new IllegalArgumentException(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name()));
		
		if(!transacao.getSenhaCartao().equals(cartao.getSenha())) {
			throw new RuntimeException(MiniAutorizadorEnum.SENHA_INVALIDA.name());
 		}
		if(!(cartao.saldo() >= transacao.getValor())) {
			throw new RuntimeException(MiniAutorizadorEnum.SALDO_INSUFICIENTE.name());
		}
		cartao.atualizaSaldo(cartao, transacao.getValor());
		cartoesRepository.save(cartao);
	}
}