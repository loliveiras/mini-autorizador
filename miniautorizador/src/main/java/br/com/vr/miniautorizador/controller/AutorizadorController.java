package br.com.vr.miniautorizador.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vr.miniautorizador.model.CartaoModel;

@RestController
@RequestMapping("/cartoes")
public class AutorizadorController {
	
	@PostMapping
	public ResponseEntity<CartaoModel> criaCartao(CartaoModel cartaoModel){
		
		try {
			
			System.out.println();
			return ResponseEntity.status(HttpStatus.CREATED).body(cartaoModel);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartaoModel);
		}
	}
}
