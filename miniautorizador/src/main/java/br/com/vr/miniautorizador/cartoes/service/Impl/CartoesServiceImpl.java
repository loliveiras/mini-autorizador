package br.com.vr.miniautorizador.cartoes.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;
import br.com.vr.miniautorizador.cartoes.service.CartoesService;
import br.com.vr.miniautorizador.enums.MiniAutorizadorEnum;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.model.Transacao;

@Service
public class CartoesServiceImpl implements CartoesService {
	
	@Autowired
	private CartoesRepository cartoesRepository;

	@Override
	public Cartao criarCartao(Cartao cartao) throws MiniAutorizadorException {
		boolean existe = cartoesRepository.existsById(cartao.getNumeroCartao());

		if (existe)
			throw new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_JA_EXISTE.name());
		return this.cartoesRepository.save(cartao);
	}

	@Override
	public Cartao obterSaldo(String numeroCartao) throws MiniAutorizadorException {
		return this.cartoesRepository
				.findById(numeroCartao)
				.orElseThrow(() -> new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name()));
	}

	@Override
	public void transacao(Transacao transacao) throws MiniAutorizadorException {

		Cartao cartao = cartoesRepository
				.findById(transacao.getNumeroCartao())
				.orElseThrow(() -> new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name()));

		verificaSenha(transacao, cartao);
		verificaSaldo(transacao, cartao);
		cartao.atualizaSaldo(cartao, transacao.getValor());

		cartoesRepository.save(cartao);
	}
	
	private void verificaSaldo(Transacao transacao, Cartao cartao) throws MiniAutorizadorException {
		if(!(cartao.getSaldo() >= transacao.getValor())) {
			throw new MiniAutorizadorException(MiniAutorizadorEnum.SALDO_INSUFICIENTE.name());
		}
	}

	private void verificaSenha(Transacao transacao, Cartao cartao) throws MiniAutorizadorException {
		if(!transacao.getSenhaCartao().equals(cartao.getSenha())) {
			throw new MiniAutorizadorException(MiniAutorizadorEnum.SENHA_INVALIDA.name());
 		}
	}
}