package com.expense.app.user.dto.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserChangePasswordCommand {
	
	@NotBlank(message = "{validation.required}")
	@Size(min = 5, max = 50, message = "{validation.size}")
	private String password;
	
	@NotBlank(message = "{validation.required}")
	@Size(min = 5, max = 50, message = "{validation.size}")
	private String confirmPassword;
	
	private String token;
	
	public UserChangePasswordCommand() {
	}
	
	public UserChangePasswordCommand(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
