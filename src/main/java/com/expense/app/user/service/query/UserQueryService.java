package com.expense.app.user.service.query;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.user.entity.ResetPasswordTokenEntity;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.exception.ResetPasswordTokenException;
import com.expense.app.user.exception.UserNotFoundException;
import com.expense.app.user.repo.ResetPasswordTokenRepo;
import com.expense.app.user.repo.UserRepo;

@Service
@Transactional(readOnly = true)
public class UserQueryService {
	
	private UserRepo userRepo;

	private ResetPasswordTokenRepo resetTokenRepo;
	
	@Value("${resetToken.expirationTime}")
	private long expirationTime;
	
	public UserQueryService(UserRepo userRepo, ResetPasswordTokenRepo resetTokenRepo) {
		this.userRepo = userRepo;
		this.resetTokenRepo = resetTokenRepo;
	}

	public void validateResetPasswordToken(String token) {
		ResetPasswordTokenEntity resetToken = resetTokenRepo.findByToken(token)
				.orElseThrow(() -> new ResetPasswordTokenException("Your token is no longer valid."));
		
		LocalDateTime expirationDate = resetToken.getCreatedAt().plusSeconds(expirationTime);
		if (LocalDateTime.now().isAfter(expirationDate)) {
			throw new ResetPasswordTokenException("Your token has expired.");
		}		
	}

	public UserEntity getUser(String username) {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User [" + username + "] does not exist."));
	}	
}
