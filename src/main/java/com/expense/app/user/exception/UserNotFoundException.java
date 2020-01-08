package com.expense.app.user.exception;

public class UserNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1087493905573312236L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
