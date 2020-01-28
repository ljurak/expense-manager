package com.expense.app.user.dto.command;

public class UserActivateCommand {
	
	private String token;
	
	public UserActivateCommand(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
