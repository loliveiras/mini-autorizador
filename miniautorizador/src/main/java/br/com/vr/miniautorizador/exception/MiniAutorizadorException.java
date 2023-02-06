package br.com.vr.miniautorizador.exception;

import lombok.Getter;

public class MiniAutorizadorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	@Getter
	private String message;

	public MiniAutorizadorException(String message) {
		this.message = message;
	}
}
