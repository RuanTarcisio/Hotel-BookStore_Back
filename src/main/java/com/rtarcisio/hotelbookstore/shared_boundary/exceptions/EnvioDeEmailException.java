package com.rtarcisio.hotelbookstore.shared_boundary.exceptions;

public class EnvioDeEmailException extends RuntimeException {
private static final long serialVersionUID = 1L;
	
	public EnvioDeEmailException(String msg) {
		super(msg);
	}
	
}
