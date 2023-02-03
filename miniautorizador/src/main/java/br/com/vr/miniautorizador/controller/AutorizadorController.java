package br.com.vr.miniautorizador.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.service.AutorizadorService;

@RestController
@RequestMapping("/cartoes")
public class AutorizadorController {
	
	@Autowired
	private AutorizadorService autorizadorService;

	@PostMapping
	public ResponseEntity<Cartao> criaCartao(@RequestBody Cartao cartaoModel){
		
		try {
			
			autorizadorService.verificarSeCartaoExiste(cartaoModel.getNumeroCartao());
			
			autorizadorService.save(cartaoModel);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(cartaoModel);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartaoModel);
		}
	}
}
