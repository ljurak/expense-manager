package com.expense.app.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.dto.query.ExpenseFilterQuery;
import com.expense.app.expense.dto.query.ExpenseReportQuery;
import com.expense.app.expense.service.query.ExpenseQueryService;
import com.expense.app.user.dto.command.UserChangePasswordCommand;
import com.expense.app.user.dto.command.UserRegisterCommand;
import com.expense.app.user.dto.command.UserResetPasswordCommand;
import com.expense.app.user.dto.command.UserUpdatePasswordCommand;
import com.expense.app.user.exception.ResetPasswordTokenException;
import com.expense.app.user.service.query.UserQueryService;

@Controller
public class UserQueryController {
	
	public UserQueryService userService;
	
	public ExpenseQueryService expenseService;
	
	public UserQueryController(UserQueryService userService, ExpenseQueryService expenseService) {
		this.userService = userService;
		this.expenseService = expenseService;
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
	
	@GetMapping("/change-password")
	public String showChangePasswordPage(@RequestParam("token") String token, Model model) {
		userService.validateResetPasswordToken(token);
		model.addAttribute("changePasswordCommand", new UserChangePasswordCommand(token));
		return "changePassword";
	}
	
	@GetMapping("/user")
	public String showUserPage(Authentication authentication, Model model) {
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		model.addAttribute("user", userService.getUser(username));
		model.addAttribute("updatePasswordCommand", new UserUpdatePasswordCommand());
		model.addAttribute("expenseCategories", expenseService.getCategories());
		model.addAttribute("expenseCreateCommand", new ExpenseCreateCommand());
		model.addAttribute("expenseFilterQuery", new ExpenseFilterQuery());
		model.addAttribute("expenseReportQuery", new ExpenseReportQuery());		
		return "userPage";
	}
	
	@ExceptionHandler(ResetPasswordTokenException.class)
	public String handleResetPasswordTokenException(ResetPasswordTokenException exception) {
		return "redirect:/reset-password?error";
	}
}
