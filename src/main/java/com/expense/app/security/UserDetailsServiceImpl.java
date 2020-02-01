package com.expense.app.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.repo.UserRepo;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private UserRepo userRepo;
	
	public UserDetailsServiceImpl(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepo.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User " + username + " does not exist"));
		
		User user = new User(userEntity);
		return user;
	}
}
