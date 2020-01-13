package com.expense.app.expense.controller;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.dto.command.ExpenseDeleteCommand;
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
	
	@PostMapping("/expenses/create")
	public String createExpense(@ModelAttribute("expenseCreateCommand") @Valid ExpenseCreateCommand command, BindingResult result,
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
	
	@GetMapping("/expenses/delete/{id}")
	public String deleteExpense(@PathVariable long id, Authentication authentication, @RequestHeader("Referer") String refererHeader) {
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		ExpenseDeleteCommand command = new ExpenseDeleteCommand(id, username);
		
		expenseService.deleteExpense(command);
		
		String redirectUrl = refererHeader.substring(refererHeader.indexOf("/expenses"));
		return "redirect:" + redirectUrl;
	}
}
