package com.expense.app.user.dto.command;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserResetPasswordCommand {
	
	@NotBlank(message = "{validation.required}")
	@Size(min = 5, max = 100, message = "{validation.size}")
	@Email(message = "{validation.email}")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
