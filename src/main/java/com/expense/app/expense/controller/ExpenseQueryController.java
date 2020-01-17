package com.expense.app.expense.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.dto.query.ExpenseFilterQuery;
import com.expense.app.expense.dto.query.ExpenseReportQuery;
import com.expense.app.expense.entity.CategoryEntity;
import com.expense.app.expense.entity.ExpenseEntity;
import com.expense.app.expense.repo.CategoryRepo;
import com.expense.app.expense.repo.ExpenseRepo;
import com.expense.app.expense.repo.ExpenseFilterSpecification;

@Controller
public class ExpenseQueryController {
	
	private ExpenseRepo expenseRepo;
	
	private CategoryRepo categoryRepo;
	
	@Value("${expenses.pageSize}")
	private int pageSize;
	
	public ExpenseQueryController(ExpenseRepo expenseRepo, CategoryRepo categoryRepo) {
		this.expenseRepo = expenseRepo;
		this.categoryRepo = categoryRepo;
	}
	
	@ModelAttribute("expenseCategories")
	public Iterable<CategoryEntity> populateCategories() {
		return categoryRepo.findAll();
	}
	
	@GetMapping("/expenses")
	public String showExpensesPage(
			@RequestParam(required = false, defaultValue = "0") int page, 
			Authentication authentication, 
			Model model) {
		
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		
		Page<ExpenseEntity> expensesPage = expenseRepo.findByUsername(
				username, PageRequest.of(page, pageSize, Sort.by("date").descending()));
		
		model.addAttribute("expenseCreateCommand", new ExpenseCreateCommand());
		model.addAttribute("expenseReportQuery", new ExpenseReportQuery());
		model.addAttribute("expenseFilterQuery", new ExpenseFilterQuery());
		model.addAttribute("expensesList", expensesPage.getContent());
		model.addAttribute("pageCount", expensesPage.getTotalPages());
		model.addAttribute("currentPage", expensesPage.getNumber());
		return "home";
	}
	
	@GetMapping("/expenses/show")
	public String showFilteredExpensesPage(
			@ModelAttribute("expenseFilterQuery") @Valid ExpenseFilterQuery query, 
			BindingResult result, 
			@RequestParam(required = false, defaultValue = "0") int page, 
			Authentication authentication, 
			Model model) {
		
		model.addAttribute("expenseCreateCommand", new ExpenseCreateCommand());
		model.addAttribute("expenseReportQuery", new ExpenseReportQuery());
		
		if (result.hasErrors()) {
			return "expensesPage";
		}
		
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		query.setUsername(username);
		
		Specification<ExpenseEntity> specification = new ExpenseFilterSpecification(query);
		Page<ExpenseEntity> pageWrapper = 
				expenseRepo.findAll(specification, PageRequest.of(page, pageSize, Sort.by("date").descending()));
		
		model.addAttribute("expensesList", pageWrapper.getContent());
		model.addAttribute("pageCount", pageWrapper.getTotalPages());
		model.addAttribute("currentPage", pageWrapper.getNumber());
		
		return "expensesPage";
	}
}
