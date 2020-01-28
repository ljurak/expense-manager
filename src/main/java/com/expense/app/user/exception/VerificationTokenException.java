package com.expense.app.user.exception;

public class VerificationTokenException extends RuntimeException {
	
	private static final long serialVersionUID = 6832380518828030835L;

	public VerificationTokenException(String message) {
		super(message);
	}
}
