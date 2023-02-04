package br.com.vr.miniautorizador.cartoes.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.cartoes.service.Impl.CartoesServiceImpl;

@RunWith(SpringRunner.class)
public class CartoesServiceImplTest {
	
	static Cartao cartao;
	
	@BeforeAll
	protected static void populaCartao() {
		cartao = new Cartao("1234", "6549873025634501");
	}
	
	@TestConfiguration
	static class CartoesServiceImplConfiguration {
		
		@Bean
		public CartoesService cartoesService() {
			return new CartoesServiceImpl();
		}
	}
	
	@Autowired
	private CartoesService cartoesService;
	
	@Test
	public void saveTest() {
		cartoesService.criar(cartao);
	}
	
	@Test
	public void findTest() throws Exception {
		cartoesService.obterSaldo(cartao.getNumeroCartao());
	}
}