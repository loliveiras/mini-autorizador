package br.com.vr.miniautorizador.transacoes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vr.miniautorizador.cartoes.controller.CartoesController;
import br.com.vr.miniautorizador.cartoes.service.CartoesService;
import br.com.vr.miniautorizador.enums.MiniAutorizadorEnum;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.model.Transacao;

@RestController
@RequestMapping("/transacoes")
public class TransacoesController {
	
	private static Logger logger = LoggerFactory.getLogger(CartoesController.class);

	@Autowired
	private CartoesService cartoesService;

	@PostMapping
	public ResponseEntity<String> transacao(@RequestBody Transacao transacao) throws MiniAutorizadorException {
		logger.info("Iniciando método transacao()");

		try {
			cartoesService.transacao(transacao);
			
			logger.info("Transacao do cartão " + transacao.getNumeroCartao() + " finalizada com sucesso");
			return ResponseEntity.status(HttpStatus.CREATED).body(MiniAutorizadorEnum.OK.name());

		} catch (MiniAutorizadorException e) {
			
			logger.info("Transação do cartão " + transacao.getNumeroCartao() + " negada pelo motivo: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
		}
	}
}
