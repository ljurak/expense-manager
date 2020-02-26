package com.expense.app.user.service.command;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.common.cqrs.command.CommandHandler;
import com.expense.app.user.dto.command.UserNotVerifiedDeleteCommand;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.entity.VerificationTokenEntity;
import com.expense.app.user.repo.UserRepo;
import com.expense.app.user.repo.VerificationTokenRepo;

@Service
public class UserNotVerifiedDeleteCommandHandler implements CommandHandler<UserNotVerifiedDeleteCommand> {
	
	private UserRepo userRepo;
	
	private VerificationTokenRepo verificationTokenRepo;
	
	public UserNotVerifiedDeleteCommandHandler(UserRepo userRepo, VerificationTokenRepo verificationTokenRepo) {
		this.userRepo = userRepo;
		this.verificationTokenRepo = verificationTokenRepo;
	}

	@Override
	@Transactional
	public void handle(UserNotVerifiedDeleteCommand command) {
		Optional<VerificationTokenEntity> verificationToken = verificationTokenRepo.findById(command.getTokenId());
		if (verificationToken.isPresent()) {
			UserEntity user = verificationToken.get().getUser();
			verificationTokenRepo.delete(verificationToken.get());
			userRepo.delete(user);
		}		
	}	
}
