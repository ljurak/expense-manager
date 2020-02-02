package com.expense.app.user.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.mail.service.EmailFactory;
import com.expense.app.mail.service.EmailService;
import com.expense.app.user.dto.command.ResetPasswordTokenDeleteCommand;
import com.expense.app.user.dto.command.UserActivateCommand;
import com.expense.app.user.dto.command.UserChangePasswordCommand;
import com.expense.app.user.dto.command.UserNotVerifiedDeleteCommand;
import com.expense.app.user.dto.command.UserRegisterCommand;
import com.expense.app.user.dto.command.UserResetPasswordCommand;
import com.expense.app.user.dto.command.UserUpdateCommand;
import com.expense.app.user.entity.ResetPasswordTokenEntity;
import com.expense.app.user.entity.RoleEntity;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.entity.VerificationTokenEntity;
import com.expense.app.user.exception.ResetPasswordTokenException;
import com.expense.app.user.exception.RoleNotFoundException;
import com.expense.app.user.exception.UserNotAvailableException;
import com.expense.app.user.exception.UserNotFoundException;
import com.expense.app.user.exception.VerificationTokenException;
import com.expense.app.user.repo.ResetPasswordTokenRepo;
import com.expense.app.user.repo.RoleRepo;
import com.expense.app.user.repo.UserRepo;
import com.expense.app.user.repo.VerificationTokenRepo;

@Service
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

	private UserRepo userRepo;
	
	private RoleRepo roleRepo;
	
	private VerificationTokenRepo verificationTokenRepo;
	
	private ResetPasswordTokenRepo resetTokenRepo;
	
	private PasswordEncoder passwordEncoder;
	
	private EmailService emailService;
	
	@Value("${verificationToken.expirationTime}")
	private long expirationTime;
	
	public UserCommandServiceImpl(
			UserRepo userRepo, 
			RoleRepo roleRepo, 
			VerificationTokenRepo verificationTokenRepo,
			ResetPasswordTokenRepo resetTokenRepo,
			PasswordEncoder passwordEncoder,
			EmailService emailService) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.verificationTokenRepo = verificationTokenRepo;
		this.resetTokenRepo = resetTokenRepo;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}
	
	@Override
	public void registerUser(UserRegisterCommand command) {
		validateUsernameAndEmail(command);		
		UserEntity user = createUser(command);		
		VerificationTokenEntity verificationToken = createVerificationToken(user);
		emailService.sendEmail(EmailFactory.createVerificationEmail(user.getEmail(), verificationToken.getToken()));		
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
	
	@Override
	public void activateUser(UserActivateCommand command) {
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
	
	@Override
	public void deleteNotVerifiedUser(UserNotVerifiedDeleteCommand command) {
		VerificationTokenEntity verificationToken = verificationTokenRepo.findById(command.getTokenId()).orElse(null);
		if (verificationToken != null) {
			UserEntity user = verificationToken.getUser();
			verificationTokenRepo.delete(verificationToken);
			userRepo.delete(user);
		}		
	}
	
	@Override
	public void resetPassword(UserResetPasswordCommand command) {
		UserEntity user = userRepo.findByEmail(command.getEmail())
				.orElseThrow(() -> new UserNotFoundException("User with email [" + command.getEmail() + "] has not been found."));
		if (!resetTokenRepo.existsByUser(user)) {
			ResetPasswordTokenEntity resetToken = createResetToken(user);
			emailService.sendEmail(EmailFactory.createResetPasswordEmail(command.getEmail(), resetToken.getToken()));	
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
	
	@Override
	public void changePassword(UserChangePasswordCommand command) {
		ResetPasswordTokenEntity resetToken = resetTokenRepo.findByToken(command.getToken())
				.orElseThrow(() -> new ResetPasswordTokenException("Your token is no longer valid."));
		validateResetToken(resetToken);
		UserEntity user = resetToken.getUser();
		user.setPassword("{bcrypt}" + passwordEncoder.encode(command.getPassword()));
		resetTokenRepo.delete(resetToken);
	}
	
	private void validateResetToken(ResetPasswordTokenEntity resetToken) {
		LocalDateTime expirationDate = resetToken.getCreatedAt().plusSeconds(expirationTime);
		if (LocalDateTime.now().isAfter(expirationDate)) {
			throw new ResetPasswordTokenException("Your token has expired.", resetToken.getId());
		}		
	}
	
	@Override
	public void deleteResetToken(ResetPasswordTokenDeleteCommand command) {
		ResetPasswordTokenEntity resetToken = resetTokenRepo.findById(command.getTokenId()).orElse(null);
		if (resetToken != null) {
			resetTokenRepo.delete(resetToken);
		}
	}
	
	@Override
	public void updateUser(UserUpdateCommand command) {
		// TODO Auto-generated method stub		
	}
}
