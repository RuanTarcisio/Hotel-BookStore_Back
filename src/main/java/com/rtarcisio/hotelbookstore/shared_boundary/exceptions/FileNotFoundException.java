package com.rtarcisio.hotelbookstore.shared_boundary.exceptions;

public class FileNotFoundException extends RuntimeException {

private static final long serialVersionUID = 1L;

	public FileNotFoundException(String msg) {
		super(msg);
	}
}
