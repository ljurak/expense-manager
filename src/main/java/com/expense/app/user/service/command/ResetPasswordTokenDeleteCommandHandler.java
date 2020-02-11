package com.expense.app.user.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.common.cqrs.command.CommandHandler;
import com.expense.app.user.dto.command.ResetPasswordTokenDeleteCommand;
import com.expense.app.user.entity.ResetPasswordTokenEntity;
import com.expense.app.user.repo.ResetPasswordTokenRepo;

@Service
public class ResetPasswordTokenDeleteCommandHandler implements CommandHandler<ResetPasswordTokenDeleteCommand> {
	
	private ResetPasswordTokenRepo resetTokenRepo;
	
	public ResetPasswordTokenDeleteCommandHandler(ResetPasswordTokenRepo resetTokenRepo) {
		this.resetTokenRepo = resetTokenRepo;
	}

	@Override
	@Transactional
	public void handle(ResetPasswordTokenDeleteCommand command) {
		ResetPasswordTokenEntity resetToken = resetTokenRepo.findById(command.getTokenId()).orElse(null);
		if (resetToken != null) {
			resetTokenRepo.delete(resetToken);
		}		
	}	
}
