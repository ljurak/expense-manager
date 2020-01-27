package com.expense.app.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.expense.app.user.dto.command.UserRegisterCommand;

@Controller
public class UserQueryController {
	
	@GetMapping("/register-user")
	public String showRegisterForm(Model model) {
		model.addAttribute("userCommand", new UserRegisterCommand());
		return "registerUser";
	}
}
