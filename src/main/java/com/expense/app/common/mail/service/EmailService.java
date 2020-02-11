package com.expense.app.common.mail.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	void sendEmail(SimpleMailMessage mail);
}
