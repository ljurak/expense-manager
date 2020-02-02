package com.expense.app.user.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.user.entity.ResetPasswordTokenEntity;
import com.expense.app.user.exception.ResetPasswordTokenException;
import com.expense.app.user.repo.ResetPasswordTokenRepo;

@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

	private ResetPasswordTokenRepo resetTokenRepo;
	
	@Value("${resetToken.expirationTime}")
	private long expirationTime;
	
	public UserQueryServiceImpl(ResetPasswordTokenRepo resetTokenRepo) {
		this.resetTokenRepo = resetTokenRepo;
	}

	@Override
	public void validateResetPasswordToken(String token) {
		ResetPasswordTokenEntity resetToken = resetTokenRepo.findByToken(token)
				.orElseThrow(() -> new ResetPasswordTokenException("Your token is no longer valid."));
		validateResetToken(resetToken);
	}
	
	private void validateResetToken(ResetPasswordTokenEntity resetToken) {
		LocalDateTime expirationDate = resetToken.getCreatedAt().plusSeconds(expirationTime);
		if (LocalDateTime.now().isAfter(expirationDate)) {
			throw new ResetPasswordTokenException("Your token has expired.");
		}		
	}
}
