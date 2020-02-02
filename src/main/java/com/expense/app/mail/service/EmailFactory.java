package com.expense.app.mail.service;

import org.springframework.mail.SimpleMailMessage;

public class EmailFactory {
	
	public static SimpleMailMessage createVerificationEmail(String to, String token) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(to);
		mail.setSubject("Activate your account!");
		mail.setText("Thank you for registering in our service.\n"
				+ "To activate your account please click the following link:\n"
				+ "http://localhost:8080/activate-user?token=" + token);
		return mail;		
	}
	
	public static SimpleMailMessage createResetPasswordEmail(String to, String token) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(to);
		mail.setSubject("Reset password request");
		mail.setText("In order to reset your password click the following link:\n"
				+ "http://localhost:8080/change-password?token=" + token);
		return mail;
	}
}
