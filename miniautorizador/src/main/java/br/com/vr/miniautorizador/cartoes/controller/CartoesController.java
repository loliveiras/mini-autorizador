package br.com.vr.miniautorizador.cartoes.controller;

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

@RestController
@RequestMapping("/cartoes")
public class CartoesController {

	@Autowired
	private CartoesService cartoesService;

	@PostMapping
	public ResponseEntity<Cartao> criaCartao(@RequestBody Cartao cartao) {

		try {
			cartoesService.criar(cartao);
			return ResponseEntity.status(HttpStatus.CREATED).body(cartao);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
		}
	}

	@GetMapping("{numeroCartao}")
	public ResponseEntity<Double> obterSaldo(@PathVariable String numeroCartao) throws Exception {

		try {
			Cartao cartao = cartoesService.obterSaldo(numeroCartao);
			return ResponseEntity.status(HttpStatus.OK).body(cartao.getSaldo());

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}