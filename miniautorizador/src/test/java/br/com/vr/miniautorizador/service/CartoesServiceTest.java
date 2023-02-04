package br.com.vr.miniautorizador.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.service.Impl.CartoesServiceImpl;

@RunWith(SpringRunner.class)
public class CartoesServiceTest {
	
	static Cartao cartaoModel;
	
	@BeforeAll
	protected static void populaCartao() {
		cartaoModel = new Cartao("1234", "6549873025634501");
	}
	
	@TestConfiguration
	static class AutorizadorServiceConfiguration {
		
		@Bean
		public CartoesServiceImpl autorizadorService() {
			return new CartoesServiceImpl();
		}
	}
	
	@Autowired
	private CartoesServiceImpl autorizadorService;
	
	@Test
	public void saveTest() {
		autorizadorService.criar(cartaoModel);
	}
	
	@Test
	public void findTest() throws Exception {
		autorizadorService.obterSaldo(cartaoModel.getNumeroCartao());
	}
}