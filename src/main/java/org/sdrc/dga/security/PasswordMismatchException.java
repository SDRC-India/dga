package org.sdrc.dga.security;

public class PasswordMismatchException extends RuntimeException {

	private static final long serialVersionUID = 6113560192117626028L;

	public PasswordMismatchException() {
		super();
	}

	public PasswordMismatchException(String arg0) {
		super(arg0);
	}

	public PasswordMismatchException(Throwable arg0) {
		super(arg0);
	}

}
