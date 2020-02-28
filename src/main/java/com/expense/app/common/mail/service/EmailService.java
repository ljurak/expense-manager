package com.expense.app.common.mail.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
	
	private JavaMailSender mailSender;
	private TemplateEngine templateEngine;
	
	public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}

	@Async
	public void sendEmail(SimpleMailMessage mail) {
		mailSender.send(mail);		
	}

	@Async
	public void sendEmail(MimeMessage mail) {
		mailSender.send(mail);
	}
	
	@Async
	public void sendRegistrationEmail(String to, String token, String verifyUrl) {
		Context ctx = new Context();
		ctx.setVariable("link", verifyUrl + "?token=" + token);
		String text = templateEngine.process("mail/registration", ctx);
		
		MimeMessage mail = mailSender.createMimeMessage();		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mail);
			helper.setTo(to);
			helper.setFrom("noreply@expense-manager.mail.com");
			helper.setSubject("Activate your account!");
			helper.setText(text, true);
		} catch (MessagingException ex) {
			throw new RuntimeException(ex);
		}
		
		mailSender.send(mail);
	}
	
	@Async
	public void sendResetPasswordEmail(String to, String token, String resetUrl) {
		Context ctx = new Context();
		ctx.setVariable("link", resetUrl + "?token=" + token);
		String text = templateEngine.process("mail/resetPassword", ctx);
		
		MimeMessage mail = mailSender.createMimeMessage();		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mail);
			helper.setTo(to);
			helper.setFrom("noreply@expense-manager.mail.com");
			helper.setSubject("Reset password request!");
			helper.setText(text, true);
		} catch (MessagingException ex) {
			throw new RuntimeException(ex);
		}
		
		mailSender.send(mail);
	}
	
	@Async
	public void sendPdfReportEmail(String to, byte[] pdfReport) {
		Context ctx = new Context();
		String text = templateEngine.process("mail/pdfReport", ctx);
		
		MimeMessage mail = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			helper.setTo(to);
			helper.setFrom("noreply@expense-manager.mail.com");
			helper.setSubject("Expenses pdf report!");
			helper.setText(text, true);
			ByteArrayResource report = new ByteArrayResource(pdfReport);
			helper.addAttachment("report.pdf", report);
		} catch (MessagingException ex) {
			throw new RuntimeException(ex);
		}
		
		mailSender.send(mail);
	}
}
