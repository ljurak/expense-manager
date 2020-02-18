package com.expense.app.user.dto.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.expense.app.common.cqrs.command.Command;

public class UserUpdatePasswordCommand implements Command {
	
	@NotBlank(message = "{validation.required}")
	@Size(min = 5, max = 50, message = "{validation.size}")
	private String currentPassword;
	
	@NotBlank(message = "{validation.required}")
	@Size(min = 5, max = 50, message = "{validation.size}")
	private String newPassword;
	
	@NotBlank(message = "{validation.required}")
	@Size(min = 5, max = 50, message = "{validation.size}")
	private String confirmPassword;
	
	private String username;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
