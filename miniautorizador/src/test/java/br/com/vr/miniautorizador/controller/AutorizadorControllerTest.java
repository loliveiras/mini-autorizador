package br.com.vr.miniautorizador.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.vr.miniautorizador.model.CartaoModel;

@SpringBootTest
@AutoConfigureMockMvc
public class AutorizadorControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void criaCartao() throws Exception {
		
		String senha = "1234";
		String numeroCartao = "6549873025634501";
		CartaoModel cartao = new CartaoModel(senha, numeroCartao);
		
		mockMvc.perform(post("/cartoes")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(cartao)))
		.andExpect(status().isCreated());
	}
	
	@Test
	public void criaCartaoExistente() throws Exception {
		
		String senha = "1234";
		String numeroCartao = "6549873025634501";
		CartaoModel cartao = new CartaoModel(senha, numeroCartao);
		
		mockMvc.perform(post("/cartoes")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(cartao)))
		.andExpect(status().isUnprocessableEntity());
	}
}