package com.expense.app.user.service;

import com.expense.app.user.dto.command.ResetPasswordTokenDeleteCommand;
import com.expense.app.user.dto.command.UserActivateCommand;
import com.expense.app.user.dto.command.UserChangePasswordCommand;
import com.expense.app.user.dto.command.UserNotVerifiedDeleteCommand;
import com.expense.app.user.dto.command.UserRegisterCommand;
import com.expense.app.user.dto.command.UserResetPasswordCommand;
import com.expense.app.user.dto.command.UserUpdateCommand;

public interface UserCommandService {
	void registerUser(UserRegisterCommand command);
	void activateUser(UserActivateCommand command);
	void deleteNotVerifiedUser(UserNotVerifiedDeleteCommand command);
	void resetPassword(UserResetPasswordCommand command);
	void changePassword(UserChangePasswordCommand command);
	void deleteResetToken(ResetPasswordTokenDeleteCommand command);
	void updateUser(UserUpdateCommand command);
}
