package com.expense.app.user.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.expense.app.user.dto.command.UserRegisterCommand;
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
		return "redirect:/login";
	}
}
