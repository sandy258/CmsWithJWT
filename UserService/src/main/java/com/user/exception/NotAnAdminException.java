package com.user.exception;

public class NotAnAdminException extends Exception{

	private static final long serialVersionUID = 1L;
	public NotAnAdminException(String message) {
		super(message);
	}

}
