package com.rtarcisio.hotelbookstore.shared.exceptions;

public class ObjetoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ObjetoNaoEncontradoException(String msg) {
		super(msg);
	}

}
