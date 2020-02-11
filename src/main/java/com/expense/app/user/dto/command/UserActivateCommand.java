package com.expense.app.user.dto.command;

import com.expense.app.common.cqrs.command.Command;

public class UserActivateCommand implements Command {
	
	private String token;
	
	public UserActivateCommand(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
