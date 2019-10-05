package org.sdrc.dga.security;

public class UserAlreadyExistException  extends RuntimeException{
	private static final long serialVersionUID = 6113560192117626028L;

	public UserAlreadyExistException() {
		super();
	}

	public UserAlreadyExistException(String arg0) {
		super(arg0);
	}

	public UserAlreadyExistException(Throwable arg0) {
		super(arg0);
	}
}
