package com.rtarcisio.hotelbookstore.shared_boundary.exceptions;

public class DadoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DadoInvalidoException(String msg) {
		super(msg);
	}

}
