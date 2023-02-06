package br.com.vr.miniautorizador.transacoes.controller;

import static org.mockito.Mockito.when;
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
import br.com.vr.miniautorizador.transacoes.model.Transacao;

@SpringBootTest
@AutoConfigureMockMvc
public class TransacoesControllerTest {

	private static Transacao transacao;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CartoesRepository cartoesRepository;

	@Test
	public void transacaoCartaoTest() throws Exception {
		
		Cartao cartao = new Cartao("6549873025634501", "1234");

		when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(Optional.of(cartao));

		this.mockMvc.perform(post("/transacoes")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(transacao)))
		.andExpect(status().isCreated());
	}
	
	@Test
	public void transacaoCartaoNegadaTest() throws Exception {
		
		when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(Optional.empty());

		this.mockMvc.perform(post("/transacoes")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(transacao)))
		.andExpect(status().isUnprocessableEntity());
	}

	@BeforeAll
	protected static void populaTransacao() {
		transacao = new Transacao("6549873025634501", "1234", 10.0);
	}
}