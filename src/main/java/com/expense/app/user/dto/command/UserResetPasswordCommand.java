package com.expense.app.user.dto.command;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.expense.app.common.cqrs.command.Command;

public class UserResetPasswordCommand implements Command {
	
	@NotBlank(message = "{validation.required}")
	@Size(min = 5, max = 100, message = "{validation.size}")
	@Email(message = "{validation.email}")
	private String email;
	
	private String resetUrl;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResetUrl() {
		return resetUrl;
	}

	public void setResetUrl(String resetUrl) {
		this.resetUrl = resetUrl;
	}
}
