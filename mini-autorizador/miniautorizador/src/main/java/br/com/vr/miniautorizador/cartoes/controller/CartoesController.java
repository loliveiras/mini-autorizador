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
import br.com.vr.miniautorizador.cartoes.service.CartaoService;
import br.com.vr.miniautorizador.enums.MiniAutorizadorEnum;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.model.TransacaoCartao;

@RestController
@RequestMapping
public class CartoesController {
	
	private static Logger logger = LoggerFactory.getLogger(CartoesController.class);

	@Autowired
	private CartaoService cartaoService;
	
	@PostMapping("/cartoes")
	public ResponseEntity<Cartao> registrarCartao(@RequestBody Cartao cartao) {
		logger.info("Iniciando método registrarCartao()");

		try {
			cartaoService.registrarCartao(cartao);
			logger.info("Cartão " + cartao.getNumeroCartao() + " registrado com sucesso");
			return ResponseEntity.status(HttpStatus.CREATED).body(cartao);
			
		} catch (MiniAutorizadorException e) {
			logger.info("Cartão " + cartao.getNumeroCartao() + " já existe cadastrado no sistema");
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
		}
	}
	@GetMapping("/cartoes/{numeroCartao}")
	public ResponseEntity<Double> obterSaldoCartao(@PathVariable String numeroCartao) {
		logger.info("Iniciando método obterSaldoCartao()");

		try {
			logger.info("Saldo do Cartão " + numeroCartao + " obtido com sucesso");
			return ResponseEntity.status(HttpStatus.OK).body(cartaoService.obterSaldoCartao(numeroCartao));
		} catch (MiniAutorizadorException e) {
			logger.info("Cartao " + numeroCartao + " não encontrado no sistema");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	@PostMapping("/transacoes")
	public ResponseEntity<String> transacaoCartao(@RequestBody TransacaoCartao transacao) throws MiniAutorizadorException {
		logger.info("Iniciando método transacao()");
		try {
			cartaoService.processarTransacao(transacao);
			logger.info("Transacao do cartão " + transacao.getNumeroCartao() + " finalizada com sucesso");
			return ResponseEntity.status(HttpStatus.CREATED).body(MiniAutorizadorEnum.OK.name());
		} catch (MiniAutorizadorException e) {
			logger.info("Transação do cartão " + transacao.getNumeroCartao() + " negada pelo motivo: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
		}
	}
}