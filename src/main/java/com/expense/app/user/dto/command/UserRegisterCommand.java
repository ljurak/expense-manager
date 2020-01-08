package com.expense.app.user.dto.command;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRegisterCommand {
	
	@NotBlank
	@Size(min = 5, max = 50)
	private String username;
	
	@NotBlank
	@Size(min = 5, max = 50)
	private String password;
	
	@NotBlank
	@Size(min = 5, max = 50)
	private String firstname;
	
	@NotBlank
	@Size(min = 5, max = 50)
	private String lastname;
	
	@NotBlank
	@Size(min = 5, max = 100)
	@Email
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserRegisterCommand ["
				+ "username=" + username 
				+ ", password=" + password 
				+ ", firstname=" + firstname
				+ ", lastname=" + lastname 
				+ ", email=" + email + "]";
	}	
}
