package com.expense.app.user.exception;

public class UsernameNotAvailableException extends RuntimeException {
	
	private static final long serialVersionUID = -4448866824326571329L;

	public UsernameNotAvailableException(String message) {
		super(message);
	}
}
