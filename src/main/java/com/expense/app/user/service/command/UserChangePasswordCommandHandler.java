package com.expense.app.user.service.command;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.common.cqrs.command.CommandHandler;
import com.expense.app.user.dto.command.UserChangePasswordCommand;
import com.expense.app.user.entity.ResetPasswordTokenEntity;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.exception.ResetPasswordTokenException;
import com.expense.app.user.repo.ResetPasswordTokenRepo;

@Service
public class UserChangePasswordCommandHandler implements CommandHandler<UserChangePasswordCommand> {
	
	private ResetPasswordTokenRepo resetTokenRepo;
	
	private PasswordEncoder passwordEncoder;
	
	@Value("${verificationToken.expirationTime}")
	private long expirationTime;
	
	@Value("{encoder.prefix}")
	private String encoderPrefix;
	
	public UserChangePasswordCommandHandler(ResetPasswordTokenRepo resetTokenRepo, PasswordEncoder passwordEncoder) {
		this.resetTokenRepo = resetTokenRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void handle(UserChangePasswordCommand command) {
		ResetPasswordTokenEntity resetToken = resetTokenRepo.findByToken(command.getToken())
				.orElseThrow(() -> new ResetPasswordTokenException("Your token is no longer valid."));
		validateResetToken(resetToken);
		UserEntity user = resetToken.getUser();
		user.setPassword(encoderPrefix + passwordEncoder.encode(command.getPassword()));
		resetTokenRepo.delete(resetToken);
	}
	
	private void validateResetToken(ResetPasswordTokenEntity resetToken) {
		LocalDateTime expirationDate = resetToken.getCreatedAt().plusSeconds(expirationTime);
		if (LocalDateTime.now().isAfter(expirationDate)) {
			throw new ResetPasswordTokenException("Your token has expired.", resetToken.getId());
		}		
	}
}
