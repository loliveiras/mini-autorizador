package br.com.vc.miniautorizador.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.vr.miniautorizador.model.Cartao;

@SpringBootTest
@AutoConfigureMockMvc
public class AutorizadorControllerTest {
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
	    mockMvc = MockMvcBuilders.standaloneSetup(meuController).build();
	}

	@Test
	@PostMapping
	public void criaCartao() {
		
		String senha = "1234";
		String numeroCartao = "6549873025634501";
		Cartao cartao = new Cartao(senha, numeroCartao);
		
		ResultActions response = mockMvc.perform(
                get("/api/show/1")
                .contentType(MediaType.TEXT_HTML)
                .header("meu header", "valor do meu header"));
	}
}
