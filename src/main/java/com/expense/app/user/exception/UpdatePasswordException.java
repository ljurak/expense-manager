package com.expense.app.user.exception;

public class UpdatePasswordException extends RuntimeException {
	
	private static final long serialVersionUID = -8718422634802343295L;

	public UpdatePasswordException(String message) {
		super(message);
	}
}
