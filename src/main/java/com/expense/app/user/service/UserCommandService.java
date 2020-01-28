package com.expense.app.user.service;

import com.expense.app.user.dto.command.UserActivateCommand;
import com.expense.app.user.dto.command.UserRegisterCommand;
import com.expense.app.user.dto.command.UserUpdateCommand;

public interface UserCommandService {
	void registerUser(UserRegisterCommand command);
	void activateUser(UserActivateCommand command);
	void updateUser(UserUpdateCommand command);
}
