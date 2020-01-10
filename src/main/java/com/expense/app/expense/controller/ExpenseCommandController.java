package com.expense.app.expense.controller;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.entity.CategoryEntity;
import com.expense.app.expense.repo.CategoryRepo;
import com.expense.app.expense.service.ExpenseCommandService;

@Controller
public class ExpenseCommandController {
	
	private ExpenseCommandService expenseService;
	
	private CategoryRepo categoryRepo;
	
	public ExpenseCommandController(ExpenseCommandService expenseService, CategoryRepo categoryRepo) {
		this.expenseService = expenseService;
		this.categoryRepo = categoryRepo;
	}
	
	@ModelAttribute("expenseCategories")
	public Iterable<CategoryEntity> populateCategories() {
		return categoryRepo.findAll();
	}
	
	@PostMapping("/expenses")
	public String createExpense(@ModelAttribute("expenseCommand") @Valid ExpenseCreateCommand command, BindingResult result,
			Authentication authentication) {
		if (result.hasErrors()) {
			return "home";
		}
		
		if (command.getDescription().length() == 0) {
			command.setDescription(null);
		}
		
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		command.setUsername(username);
		
		expenseService.createExpense(command);
		return "redirect:/expenses";
	}
}
