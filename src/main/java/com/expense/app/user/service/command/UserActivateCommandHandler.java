package com.expense.app.user.service.command;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.common.cqrs.command.CommandHandler;
import com.expense.app.user.dto.command.UserActivateCommand;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.entity.VerificationTokenEntity;
import com.expense.app.user.exception.VerificationTokenException;
import com.expense.app.user.repo.VerificationTokenRepo;

@Service
public class UserActivateCommandHandler implements CommandHandler<UserActivateCommand> {

	private VerificationTokenRepo verificationTokenRepo;
	
	@Value("${verificationToken.expirationTime}")
	private long expirationTime;
	
	public UserActivateCommandHandler(VerificationTokenRepo verificationTokenRepo) {
		this.verificationTokenRepo = verificationTokenRepo;
	}

	@Override
	@Transactional
	public void handle(UserActivateCommand command) {
		VerificationTokenEntity verificationToken = verificationTokenRepo.findByToken(command.getToken())
				.orElseThrow(() -> new VerificationTokenException("Your token is no longer valid. Please register again."));		
		validateVerificationToken(verificationToken);	
		UserEntity user = verificationToken.getUser();
		user.setEnabled(true);
		verificationTokenRepo.delete(verificationToken);	
	}
	
	private void validateVerificationToken(VerificationTokenEntity verificationToken) {
		LocalDateTime expirationDate = verificationToken.getCreatedAt().plusSeconds(expirationTime);
		if (LocalDateTime.now().isAfter(expirationDate)) {
			throw new VerificationTokenException("Your token has expired. Please register again.", verificationToken.getId());
		}
	}
}
