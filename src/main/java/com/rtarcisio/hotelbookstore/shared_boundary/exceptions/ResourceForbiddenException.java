package com.rtarcisio.hotelbookstore.shared_boundary.exceptions;

public class ResourceForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceForbiddenException(String msg) {
		super(msg);
	}

}
