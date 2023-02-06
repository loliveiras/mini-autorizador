package br.com.vr.miniautorizador.cartoes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.cartoes.service.CartoesService;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;

@RestController
@RequestMapping("/cartoes")
public class CartoesController {
	
	private static Logger logger = LoggerFactory.getLogger(CartoesController.class);

	@Autowired
	private CartoesService cartoesService;
	
	@PostMapping
	public ResponseEntity<Cartao> criarCartao(@RequestBody Cartao cartao) {
		logger.info("Iniciando método criarCartao()");

		try {
			cartoesService.criarCartao(cartao);
			
			logger.info("Cartão " + cartao.getNumeroCartao() + " criado");
			return ResponseEntity.status(HttpStatus.CREATED).body(cartao);
			
		} catch (MiniAutorizadorException e) {
			
			logger.info("Cartão " + cartao.getNumeroCartao() + " ja existe");
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
		}
	}
	
	@GetMapping("{numeroCartao}")
	public ResponseEntity<Double> obterSaldoCartao(@PathVariable String numeroCartao) {
		logger.info("Iniciando método obterSaldoCartao()");

		try {
			Cartao cartao = cartoesService.obterSaldo(numeroCartao);
			
			logger.info("Saldo do Cartão " + numeroCartao + " obtido");
			return ResponseEntity.status(HttpStatus.OK).body(cartao.getSaldo());
			
		} catch (MiniAutorizadorException e) {
			
			logger.info("Cartao " + numeroCartao + " não encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}