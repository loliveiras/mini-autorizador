package br.com.vr.miniautorizador.cartoes.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CartoesControllerTest {

	private static Cartao cartao;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CartoesRepository cartoesRepository;

	@Test
	public void criaCartaoTest() throws Exception {
		when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(false);
		when(cartoesRepository.save(cartao)).thenReturn(cartao);

		this.mockMvc.perform(post("/cartoes")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(cartao)))
		.andExpect(status().isCreated());
	}

	@Test
	public void criarCartaoExistenteTest() throws Exception {
		when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(true);

		this.mockMvc.perform(post("/cartoes")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(cartao)))
		.andExpect(status().isUnprocessableEntity());

	}

	@Test
	public void obterSaldoTest() throws Exception {
		when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));

		this.mockMvc.perform(get("/cartoes/" + cartao.getNumeroCartao())
				.contentType("application/json"))
		.andExpect(status().isOk());
	}

	@Test
	public void obterSaldoCartaoInexistenteTest() throws Exception {
		when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(Optional.empty());

		this.mockMvc.perform(get("/cartoes/" + cartao.getNumeroCartao())
				.contentType("application/json"))
		.andExpect(status().isNotFound());
	}

	@BeforeAll
	protected static void populaCartao() {
		cartao = new Cartao("1234", "6549873025634501");
	}
}