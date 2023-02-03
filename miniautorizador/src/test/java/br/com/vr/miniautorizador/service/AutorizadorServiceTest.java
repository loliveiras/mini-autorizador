package br.com.vr.miniautorizador.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.vr.miniautorizador.model.CartaoModel;

@RunWith(SpringRunner.class)
public class AutorizadorServiceTest {
	
	static CartaoModel cartaoModel;
	
	@BeforeAll
	protected static void populaCartao() {
		cartaoModel = new CartaoModel("1234", "6549873025634501");
	}
	
	@TestConfiguration
	static class AutorizadorServiceConfiguration {
		
		@Bean
		public AutorizadorService autorizadorService() {
			return new AutorizadorService();
		}
	}
	
	@Autowired
	private AutorizadorService autorizadorService;
	
	@Test
	public void saveTest() {
		autorizadorService.save(cartaoModel);
	}
	
	@Test
	public void findTest() {
		autorizadorService.find(cartaoModel.getNumeroCartao());
	}
}