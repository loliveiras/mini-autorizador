package br.com.vr.miniautorizador.cartoes.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;
import br.com.vr.miniautorizador.cartoes.service.Impl.CartoesServiceImpl;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.model.Transacao;

@ExtendWith(MockitoExtension.class)
public class CartoesServiceImplTest {

	private static Cartao cartao;

	@Mock
	private CartoesRepository cartoesRepository;

	@InjectMocks
	private CartoesServiceImpl cartoesService;

	@Test
	public void saveCartaoTest() throws MiniAutorizadorException {
		when(cartoesRepository.save(cartao)).thenReturn(cartao);
		when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(false);

		cartoesService.criarCartao(cartao);
	}

	@Test
	public void saveCartaoExisteTest() {

		when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(true);

		assertThrows(MiniAutorizadorException.class, () -> cartoesService.criarCartao(cartao));
	}

	@Test
	public void obterSaldoTest() throws MiniAutorizadorException {

		when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));

		cartoesService.obterSaldo(cartao.getNumeroCartao());
	}

	@Test
	public void obterSaldoCartaoInexistenteTest() {

		when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(Optional.empty());

		assertThrows(MiniAutorizadorException.class, () -> cartoesService.obterSaldo(cartao.getNumeroCartao()));
	}

	@Test
	public void transacaoCartaoTest() throws MiniAutorizadorException {
		Transacao transacao = populaTransacao();

		when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(Optional.of(cartao));
		when(cartoesRepository.save(cartao)).thenReturn(cartao);

		cartao.verificaSenha(transacao, cartao);
		cartao.verificaSaldo(transacao, cartao);

		cartoesService.transacao(transacao);
	}

	@Test
	public void transacaoCartaoInexistenteTest() {
		Transacao transacao = populaTransacao();

		when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(Optional.empty());

		assertThrows(MiniAutorizadorException.class, () -> cartoesService.transacao(transacao));
	}

	@Test
	public void transacaoCartaoSenhaInvalidaTest() {
		Transacao transacao = new Transacao("6549873025634501", "12345", 10.0);

		when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(Optional.of(cartao));

		assertThrows(MiniAutorizadorException.class, () -> cartoesService.transacao(transacao));
	}

	@Test
	public void transacaoCartaoSaldoInsuficienteTest() {

		Transacao transacao = new Transacao("6549873025634501", "1234", 600.0);

		when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(Optional.of(cartao));

		assertThrows(MiniAutorizadorException.class, () -> cartoesService.transacao(transacao));
	}
	
	@BeforeAll
	protected static void populaCartao() {
		cartao = new Cartao("6549873025634501", "1234");
	}

	private Transacao populaTransacao() {
		Transacao transacao = new Transacao("6549873025634501", "1234", 10.0);
		return transacao;
	}
}