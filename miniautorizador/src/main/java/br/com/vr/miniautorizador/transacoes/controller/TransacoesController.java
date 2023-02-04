package br.com.vr.miniautorizador.transacoes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vr.miniautorizador.cartoes.service.CartoesService;
import br.com.vr.miniautorizador.transacoes.model.Transacao;

@RestController
@RequestMapping("/transacoes")
public class TransacoesController {

	@Autowired
	private CartoesService cartoesService;

	@PostMapping
	public ResponseEntity<String> transacao(@RequestBody Transacao transacao) {

		try {
			cartoesService.transacao(transacao);
			return ResponseEntity.status(HttpStatus.CREATED).body("OK");

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
		
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
		}
	}
}
