package com.expense.app.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.user.dto.command.UserRegisterCommand;
import com.expense.app.user.dto.command.UserUpdateCommand;
import com.expense.app.user.entity.RoleEntity;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.exception.RoleNotFoundException;
import com.expense.app.user.exception.UsernameNotAvailableException;
import com.expense.app.user.repo.RoleRepo;
import com.expense.app.user.repo.UserRepo;

@Service
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

	private UserRepo userRepo;
	
	private RoleRepo roleRepo;
	
	private PasswordEncoder passwordEncoder;
	
	public UserCommandServiceImpl(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public void registerUser(UserRegisterCommand command) {
		if (userRepo.existsByUsername(command.getUsername())) {
			throw new UsernameNotAvailableException("Given username is not available");
		}
		
		RoleEntity roleUser = roleRepo.findByName("ROLE_USER").orElseThrow(
				() -> new RoleNotFoundException("Role USER does not exist"));
		
		UserEntity user = UserEntity.builder()
				.username(command.getUsername())
				.password("{bcrypt}" + passwordEncoder.encode(command.getPassword()))
				.firstname(command.getFirstname())
				.lastname(command.getLastname())
				.email(command.getEmail())
				.enabled(true)
				.role(roleUser)
				.build();
		
		userRepo.save(user);		
	}

	@Override
	public void updateUser(UserUpdateCommand command) {
		// TODO Auto-generated method stub
		
	}
}
