package com.expense.app.common.mail.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	void sendEmail(SimpleMailMessage mail);
	void sendEmail(MimeMessage mail);
	void sendRegistrationEmail(String to, String token, String verifyUrl);
	void sendResetPasswordEmail(String to, String token, String resetUrl);
	void sendPdfReportEmail(String to, byte[] report);
}
