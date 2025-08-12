package com.rtarcisio.hotelbookstore.shared.exceptions;

public class MailSendException extends RuntimeException{


	private static final long serialVersionUID = 1L;
	
	public MailSendException(String msg) {
		super(msg);
	}
}
