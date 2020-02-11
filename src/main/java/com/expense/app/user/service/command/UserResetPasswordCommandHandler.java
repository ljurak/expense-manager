package com.expense.app.user.service.command;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.common.cqrs.command.CommandHandler;
import com.expense.app.common.mail.service.EmailFactory;
import com.expense.app.common.mail.service.EmailService;
import com.expense.app.user.dto.command.UserResetPasswordCommand;
import com.expense.app.user.entity.ResetPasswordTokenEntity;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.exception.UserNotFoundException;
import com.expense.app.user.repo.ResetPasswordTokenRepo;
import com.expense.app.user.repo.UserRepo;

@Service
public class UserResetPasswordCommandHandler implements CommandHandler<UserResetPasswordCommand> {
	
	private UserRepo userRepo;
	
	private ResetPasswordTokenRepo resetTokenRepo;
	
	private EmailService emailService;
	
	public UserResetPasswordCommandHandler(UserRepo userRepo, ResetPasswordTokenRepo resetTokenRepo,
			EmailService emailService) {
		this.userRepo = userRepo;
		this.resetTokenRepo = resetTokenRepo;
		this.emailService = emailService;
	}

	@Override
	@Transactional
	public void handle(UserResetPasswordCommand command) {
		UserEntity user = userRepo.findByEmail(command.getEmail())
				.orElseThrow(() -> new UserNotFoundException("User with email [" + command.getEmail() + "] has not been found."));
		if (!resetTokenRepo.existsByUser(user)) {
			ResetPasswordTokenEntity resetToken = createResetToken(user);
			emailService.sendEmail(EmailFactory.createResetPasswordEmail(
					command.getEmail(), resetToken.getToken(), command.getResetUrl()));	
		}	
		
	}
	
	private ResetPasswordTokenEntity createResetToken(UserEntity user) {
		ResetPasswordTokenEntity resetToken = ResetPasswordTokenEntity.builder()
				.token(UUID.randomUUID().toString())
				.user(user)
				.createdAt(LocalDateTime.now())
				.build();		
		return resetTokenRepo.save(resetToken);
	}
}
