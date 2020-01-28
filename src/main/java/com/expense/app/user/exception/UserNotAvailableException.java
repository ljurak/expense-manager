package com.expense.app.user.exception;

import com.expense.app.user.dto.command.UserRegisterCommand;

public class UserNotAvailableException extends RuntimeException {
	
	private static final long serialVersionUID = -3134681265198759986L;
	
	private UserRegisterCommand command;
	
	public UserNotAvailableException(String message, UserRegisterCommand command) {
		super(message);
		this.command = command;
	}
	
	public UserRegisterCommand getCommand() {
		return command;
	}
}
