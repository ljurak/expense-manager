package com.expense.app.user.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.expense.app.user.dto.command.UserActivateCommand;
import com.expense.app.user.dto.command.UserRegisterCommand;
import com.expense.app.user.exception.UserNotAvailableException;
import com.expense.app.user.exception.VerificationTokenException;
import com.expense.app.user.service.UserCommandService;

@Controller
public class UserCommandController {
	
	private UserCommandService userService;
	
	public UserCommandController(UserCommandService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/register-user")
	public String registerUser(@ModelAttribute("userCommand") @Valid UserRegisterCommand command, BindingResult result) {		
		if (result.hasErrors()) {
			return "registerUser";
		}
		userService.registerUser(command);
		return "redirect:/register-user?success";
	}
	
	@GetMapping("/activate-user")
	public String activateUser(@RequestParam(name = "token") String token) {
		UserActivateCommand command = new UserActivateCommand(token);
		userService.activateUser(command);
		return "redirect:/login?active";
	}
	
	@ExceptionHandler(UserNotAvailableException.class)
	public String handleUserNotAvailableException(UserNotAvailableException exception, Model model) {
		model.addAttribute("userCommand",exception.getCommand());
		model.addAttribute("userExists", exception.getMessage());
		return "registerUser";
	}
	
	@ExceptionHandler(VerificationTokenException.class)
	public String handleVerificationTokenException(VerificationTokenException exception, Model model) {
		model.addAttribute("userCommand", new UserRegisterCommand());
		model.addAttribute("verificationFailure", exception.getMessage());
		return "registerUser";
	}
}
