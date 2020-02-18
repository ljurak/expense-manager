package com.expense.app.user.service;

import com.expense.app.user.entity.UserEntity;

public interface UserQueryService {
	UserEntity getUser(String username);
	void validateResetPasswordToken(String token);
}
