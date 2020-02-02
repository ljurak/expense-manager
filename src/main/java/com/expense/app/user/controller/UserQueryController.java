package com.expense.app.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.expense.app.user.dto.command.UserChangePasswordCommand;
import com.expense.app.user.dto.command.UserRegisterCommand;
import com.expense.app.user.dto.command.UserResetPasswordCommand;
import com.expense.app.user.exception.ResetPasswordTokenException;
import com.expense.app.user.service.UserQueryService;

@Controller
public class UserQueryController {
	
	public UserQueryService userService;
	
	public UserQueryController(UserQueryService userService) {
		this.userService = userService;
	}

	@GetMapping("/register-user")
	public String showRegisterForm(Model model) {
		model.addAttribute("userCommand", new UserRegisterCommand());
		return "registerUser";
	}
	
	@GetMapping("/reset-password")
	public String showResetPasswordForm(Model model) {
		model.addAttribute("resetCommand", new UserResetPasswordCommand());
		return "resetPassword";
	}
	
	@GetMapping(path = "/change-password")
	public String showChangePasswordPage(@RequestParam("token") String token, Model model) {
		userService.validateResetPasswordToken(token);
		model.addAttribute("changePasswordCommand", new UserChangePasswordCommand(token));
		return "changePassword";
	}
	
	@ExceptionHandler(ResetPasswordTokenException.class)
	public String handleResetPasswordTokenException(ResetPasswordTokenException exception) {
		return "redirect:/reset-password?error";
	}
}
