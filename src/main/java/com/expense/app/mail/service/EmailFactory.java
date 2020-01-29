package com.expense.app.mail.service;

import org.springframework.mail.SimpleMailMessage;

public class EmailFactory {
	
	public static SimpleMailMessage createVerificationEmail(String to, String token) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(to);
		mail.setSubject("Activate your account!");
		mail.setText("Thank you for registering in our service.\n"
				+ "To activate your account click following link:\n"
				+ "http://localhost:8080/activate-user?token=" + token);
		return mail;		
	}
}
