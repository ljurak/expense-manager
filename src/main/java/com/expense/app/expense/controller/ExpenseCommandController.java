package com.expense.app.expense.controller;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.expense.app.common.cqrs.command.CommandDispatcher;
import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.dto.command.ExpenseDeleteCommand;
import com.expense.app.expense.dto.query.ExpenseFilterQuery;
import com.expense.app.expense.dto.query.ExpenseReportQuery;
import com.expense.app.expense.entity.CategoryEntity;
import com.expense.app.expense.repo.CategoryRepo;

@Controller
public class ExpenseCommandController {
	
	private CommandDispatcher commandDispatcher;
	
	private CategoryRepo categoryRepo;
	
	public ExpenseCommandController(CategoryRepo categoryRepo, CommandDispatcher commandDispatcher) {
		this.categoryRepo = categoryRepo;
		this.commandDispatcher = commandDispatcher;
	}
	
	@ModelAttribute("expenseCategories")
	public Iterable<CategoryEntity> populateCategories() {
		return categoryRepo.findAll();
	}
	
	@PostMapping("/expenses/create")
	public String createExpense(@ModelAttribute("expenseCreateCommand") @Valid ExpenseCreateCommand command, BindingResult result,
			Authentication authentication, Model model) {
		model.addAttribute("expenseFilterQuery", new ExpenseFilterQuery());
		model.addAttribute("expenseReportQuery", new ExpenseReportQuery());
		
		if (result.hasErrors()) {
			return "expensesPage";
		}
		
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		command.setUsername(username);
		
		commandDispatcher.dispatch(command);
		return "redirect:/expenses/show";
	}
	
	@GetMapping("/expenses/delete/{id}")
	public String deleteExpense(@PathVariable long id, Authentication authentication, @RequestHeader("Referer") String refererHeader) {
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		ExpenseDeleteCommand command = new ExpenseDeleteCommand(id, username);
		
		commandDispatcher.dispatch(command);
		
		String redirectUrl = refererHeader.substring(refererHeader.indexOf("/expenses"));
		return "redirect:" + redirectUrl;
	}
}
