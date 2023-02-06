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
import br.com.vr.miniautorizador.transacoes.model.Transacao;

@ExtendWith(MockitoExtension.class)
public class CartoesServiceImplTest {

	private static Cartao cartao;

	@BeforeAll
	protected static void populaCartao() {
		cartao = new Cartao("1234", "6549873025634501");
	}

	@Mock
	private CartoesRepository cartoesRepository;

	@InjectMocks
	private CartoesServiceImpl cartoesService;

	@Test
	public void saveCartaoTest() {

		when(cartoesRepository.save(cartao)).thenReturn(cartao);
		when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(false);

		cartoesService.criar(cartao);
	}

	@Test
	public void saveCartaoExisteTest() {

		when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> cartoesService.criar(cartao));
	}

	@Test
	public void obterSaldoTest() {

		when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));

		cartoesService.obterSaldo(cartao.getNumeroCartao());
	}

	@Test
	public void obterSaldoCartaoInexistenteTest() {

		when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> cartoesService.obterSaldo(cartao.getNumeroCartao()));
	}

	@Test
	public void transacaoCartaoTest() {
		Transacao transacao = populaTransacao();

		when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));
		when(cartoesRepository.save(cartao)).thenReturn(cartao);

		cartao.verificaSenha(transacao, cartao);
		cartao.verificaSaldo(transacao, cartao);

		cartoesService.transacao(transacao);
	}

	@Test
	public void transacaoCartaoInexistenteTest() {
		Transacao transacao = populaTransacao();

		when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> cartoesService.transacao(transacao));
	}

	@Test
	public void transacaoCartaoSenhaInvalidaTest() {
		Transacao transacao = new Transacao("6549873025634501", "12345", 10.0);

		when(cartoesRepository.findById(cartao.getNumeroCartao()))
				.thenReturn(Optional.of(cartao));

		assertThrows(IllegalArgumentException.class, () -> cartoesService.transacao(transacao));
	}

	@Test
	public void transacaoCartaoSaldoInsuficienteTest() {

		Transacao transacao = new Transacao("6549873025634501", "1234", 600.0);

		when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));

		assertThrows(RuntimeException.class, () -> cartoesService.transacao(transacao));
	}

	private Transacao populaTransacao() {
		Transacao transacao = new Transacao("6549873025634501", "1234", 10.0);
		return transacao;
	}
}