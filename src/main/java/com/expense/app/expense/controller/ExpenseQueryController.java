package com.expense.app.expense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.repo.CategoryRepo;

@Controller
@RequestMapping("/expenses")
public class ExpenseQueryController {
	
	private CategoryRepo categoryRepo;
	
	public ExpenseQueryController(CategoryRepo categoryRepo) {
		this.categoryRepo = categoryRepo;
	}
	
	@GetMapping
	public String showExpensesPage(Model model) {
		model.addAttribute("expenseCommand", new ExpenseCreateCommand());
		model.addAttribute("expenseCategories", categoryRepo.findAll());
		return "home";
	}
}
