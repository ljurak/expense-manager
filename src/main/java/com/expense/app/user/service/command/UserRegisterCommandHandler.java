package com.expense.app.user.service.command;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.common.cqrs.command.CommandHandler;
import com.expense.app.common.mail.service.EmailService;
import com.expense.app.user.dto.command.UserRegisterCommand;
import com.expense.app.user.entity.RoleEntity;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.entity.VerificationTokenEntity;
import com.expense.app.user.exception.RoleNotFoundException;
import com.expense.app.user.exception.UserNotAvailableException;
import com.expense.app.user.repo.RoleRepo;
import com.expense.app.user.repo.UserRepo;
import com.expense.app.user.repo.VerificationTokenRepo;

@Service
public class UserRegisterCommandHandler implements CommandHandler<UserRegisterCommand> {
	
	private UserRepo userRepo;
	
	private RoleRepo roleRepo;
	
	private VerificationTokenRepo verificationTokenRepo;
	
	private PasswordEncoder passwordEncoder;
	
	private EmailService emailService;

	public UserRegisterCommandHandler(
			UserRepo userRepo, 
			RoleRepo roleRepo, 
			VerificationTokenRepo verificationTokenRepo,
			PasswordEncoder passwordEncoder, 
			EmailService emailService) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.verificationTokenRepo = verificationTokenRepo;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	@Override
	@Transactional
	public void handle(UserRegisterCommand command) {
		validateUsernameAndEmail(command);		
		UserEntity user = createUser(command);		
		VerificationTokenEntity verificationToken = createVerificationToken(user);
		emailService.sendRegistrationEmail(user.getEmail(), verificationToken.getToken(), command.getVerifyUrl());		
	}
	
	private void validateUsernameAndEmail(UserRegisterCommand command) {
		if (userRepo.existsByUsernameOrEmail(command.getUsername(), command.getEmail())) {
			throw new UserNotAvailableException("Given username or email is not available", command);
		}
	}
	
	private UserEntity createUser(UserRegisterCommand command) {
		RoleEntity roleUser = roleRepo.findByName("ROLE_USER")
				.orElseThrow(() -> new RoleNotFoundException("Role USER does not exist"));		
		UserEntity user = UserEntity.builder()
				.username(command.getUsername())
				.password("{bcrypt}" + passwordEncoder.encode(command.getPassword()))
				.firstname(command.getFirstname())
				.lastname(command.getLastname())
				.email(command.getEmail())
				.enabled(false)
				.role(roleUser)
				.build();		
		return userRepo.save(user);
	}
	
	private VerificationTokenEntity createVerificationToken(UserEntity user) {
		VerificationTokenEntity verificationToken = VerificationTokenEntity.builder()
				.user(user)
				.token(UUID.randomUUID().toString())
				.createdAt(LocalDateTime.now())
				.build();					
		return verificationTokenRepo.save(verificationToken);
	}
}
