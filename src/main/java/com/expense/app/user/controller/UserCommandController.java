package com.expense.app.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.expense.app.common.cqrs.command.CommandDispatcher;
import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.dto.query.ExpenseFilterQuery;
import com.expense.app.expense.dto.query.ExpenseReportQuery;
import com.expense.app.expense.service.query.ExpenseQueryService;
import com.expense.app.user.dto.command.ResetPasswordTokenDeleteCommand;
import com.expense.app.user.dto.command.UserActivateCommand;
import com.expense.app.user.dto.command.UserChangePasswordCommand;
import com.expense.app.user.dto.command.UserNotVerifiedDeleteCommand;
import com.expense.app.user.dto.command.UserRegisterCommand;
import com.expense.app.user.dto.command.UserResetPasswordCommand;
import com.expense.app.user.dto.command.UserUpdatePasswordCommand;
import com.expense.app.user.exception.ResetPasswordTokenException;
import com.expense.app.user.exception.UpdatePasswordException;
import com.expense.app.user.exception.UserNotAvailableException;
import com.expense.app.user.exception.UserNotFoundException;
import com.expense.app.user.exception.VerificationTokenException;
import com.expense.app.user.service.UserQueryService;

@Controller
public class UserCommandController {
	
	private CommandDispatcher commandDispatcher;
	
	private ExpenseQueryService expenseService;
	
	private UserQueryService userService;
	
	public UserCommandController(CommandDispatcher commandDispatcher, ExpenseQueryService expenseService, UserQueryService userService) {
		this.commandDispatcher = commandDispatcher;
		this.expenseService = expenseService;
		this.userService = userService;
	}
	
	@PostMapping("/register-user")
	public String registerUser(@ModelAttribute("userCommand") @Valid UserRegisterCommand command, BindingResult result, HttpServletRequest request) {		
		if (result.hasErrors()) {
			return "registerUser";
		}
		String verifyUrl = ServletUriComponentsBuilder
				.fromContextPath(request)
				.path("/activate-user")
				.build()
				.toUriString();
		command.setVerifyUrl(verifyUrl);
		commandDispatcher.dispatch(command);
		return "redirect:/register-user?success";
	}
	
	@GetMapping("/activate-user")
	public String activateUser(@RequestParam(name = "token") String token) {
		UserActivateCommand command = new UserActivateCommand(token);
		commandDispatcher.dispatch(command);
		return "redirect:/login?active";
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@ModelAttribute("resetCommand") @Valid UserResetPasswordCommand command, BindingResult result, HttpServletRequest request) {
		if (result.hasErrors()) {
			return "resetPassword";
		}
		String resetUrl = ServletUriComponentsBuilder
				.fromContextPath(request)
				.path("/change-password")
				.build()
				.toUriString();
		command.setResetUrl(resetUrl);
		commandDispatcher.dispatch(command);
		return "redirect:/reset-password?success";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@ModelAttribute("changePasswordCommand") @Valid UserChangePasswordCommand command, BindingResult result) {
		validateChangePassword(command, result);
		if (result.hasErrors()) {
			return "changePassword";
		}
		commandDispatcher.dispatch(command);
		return "redirect:/login?changed";
	}
	
	@PostMapping("/user/update-password")
	public String updatePassword(@ModelAttribute("updatePasswordCommand") @Valid UserUpdatePasswordCommand command, BindingResult result, Authentication authentication, Model model) {
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		command.setUsername(username);
		
		model.addAttribute("expenseCreateCommand", new ExpenseCreateCommand());
		model.addAttribute("expenseFilterQuery", new ExpenseFilterQuery());
		model.addAttribute("expenseReportQuery", new ExpenseReportQuery());
		model.addAttribute("expenseCategories", expenseService.getCategories());
		model.addAttribute("user", userService.getUser(username));
		
		validateUpdatePassword(command, result);
		if (result.hasErrors()) {
			return "userPage";
		}		
		
		commandDispatcher.dispatch(command);
		return "redirect:/user?success";
	}
	
	private void validateChangePassword(UserChangePasswordCommand command, BindingResult result) {
		if (command.getToken() == null) {
			result.rejectValue("token", "changePasswordCommand.token.required");
		}
		if (!command.getPassword().equals(command.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "changePasswordCommand.confirmPassword.mismatch");
		}
	}
	
	private void validateUpdatePassword(UserUpdatePasswordCommand command, BindingResult result) {
		if (!command.getNewPassword().equals(command.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "updatePasswordCommand.confirmPassword.mismatch");
		}
	}
	
	@ExceptionHandler(UserNotAvailableException.class)
	public String handleUserNotAvailableException(UserNotAvailableException exception, Model model) {
		model.addAttribute("userCommand",exception.getCommand());
		model.addAttribute("userExists", exception.getMessage());
		return "registerUser";
	}
	
	@ExceptionHandler(VerificationTokenException.class)
	public String handleVerificationTokenException(VerificationTokenException exception, Model model) {
		if (exception.getTokenId() != 0) {
			commandDispatcher.dispatch(new UserNotVerifiedDeleteCommand(exception.getTokenId()));
		}
		
		model.addAttribute("userCommand", new UserRegisterCommand());
		model.addAttribute("verificationError", exception.getMessage());
		return "registerUser";
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public String handleUserNotFoundException(UserNotFoundException exception) {		
		return "redirect:/reset-password?success";
	}
	
	@ExceptionHandler(ResetPasswordTokenException.class)
	public String handleResetPasswordTokenException(ResetPasswordTokenException exception) {
		if (exception.getTokenId() != 0) {
			commandDispatcher.dispatch(new ResetPasswordTokenDeleteCommand(exception.getTokenId()));
		}
		return "redirect:/reset-password?error";
	}
	
	@ExceptionHandler(UpdatePasswordException.class)
	public String handleUpdatePasswordException(UpdatePasswordException exception) {
		return "redirect:/user?error";
	}
}
