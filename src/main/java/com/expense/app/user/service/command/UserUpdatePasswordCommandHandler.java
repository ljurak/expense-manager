package com.expense.app.user.service.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.common.cqrs.command.CommandHandler;
import com.expense.app.user.dto.command.UserUpdatePasswordCommand;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.exception.UpdatePasswordException;
import com.expense.app.user.exception.UserNotFoundException;
import com.expense.app.user.repo.UserRepo;

@Service
public class UserUpdatePasswordCommandHandler implements CommandHandler<UserUpdatePasswordCommand> {
	
	private UserRepo userRepo;
	
	private PasswordEncoder passwordEncoder;
	
	@Value("${encoder.prefix}")
	private String encoderPrefix;
	
	public UserUpdatePasswordCommandHandler(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void handle(UserUpdatePasswordCommand command) {
		UserEntity user = userRepo.findByUsername(command.getUsername())
				.orElseThrow(() -> new UserNotFoundException("User [" + command.getUsername() + "] has not been found"));
		if (!passwordEncoder.matches(command.getCurrentPassword(), user.getPassword().replace(encoderPrefix, ""))) {
			throw new UpdatePasswordException("Invalid password");
		}
		user.setPassword(encoderPrefix + passwordEncoder.encode(command.getNewPassword()));
	}	
}
