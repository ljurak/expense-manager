package com.expense.app.expense.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.entity.ExpenseEntity;
import com.expense.app.expense.repo.CategoryRepo;
import com.expense.app.expense.repo.ExpenseRepo;

@Controller
@RequestMapping("/expenses")
public class ExpenseQueryController {
	
	private ExpenseRepo expenseRepo;
	
	private CategoryRepo categoryRepo;
	
	public ExpenseQueryController(ExpenseRepo expenseRepo, CategoryRepo categoryRepo) {
		this.expenseRepo = expenseRepo;
		this.categoryRepo = categoryRepo;
	}
	
	@GetMapping
	public String showExpensesPage(
			@RequestParam(required = false, defaultValue = "0") int page, 
			@RequestParam(required = false, defaultValue = "15") int size, 
			Authentication authentication, 
			Model model) {
		
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		
		Page<ExpenseEntity> expensesPage = expenseRepo.findByUsername(
				username, PageRequest.of(page, size, Sort.by("date").descending()));
		
		model.addAttribute("expenseCommand", new ExpenseCreateCommand());
		model.addAttribute("expenseCategories", categoryRepo.findAll());
		model.addAttribute("expensesList", expensesPage.getContent());
		model.addAttribute("pageCount", expensesPage.getTotalPages());
		model.addAttribute("currentPage", expensesPage.getNumber());
		return "home";
	}
}
