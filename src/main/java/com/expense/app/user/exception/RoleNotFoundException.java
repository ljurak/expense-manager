package com.expense.app.user.exception;

public class RoleNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 68229896679901420L;

	public RoleNotFoundException(String message) {
		super(message);
	}
}
