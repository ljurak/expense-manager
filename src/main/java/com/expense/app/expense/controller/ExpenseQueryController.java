package com.expense.app.expense.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.expense.app.expense.dto.result.ExpenseReportDto;
import com.expense.app.expense.entity.CategoryEntity;
import com.expense.app.expense.entity.ExpenseEntity;
import com.expense.app.expense.service.query.ExpenseQueryService;

@Controller
public class ExpenseQueryController {
	
	private ExpenseQueryService expenseService;
	
	@Value("${expenses.pageSize}")
	private int pageSize;
	
	public ExpenseQueryController(ExpenseQueryService expenseService) {
		this.expenseService = expenseService;
	}
	
	@ModelAttribute("expenseCategories")
	public Iterable<CategoryEntity> populateCategories() {
		return expenseService.getCategories();
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
		query.setPage(page);
		query.setPageSize(pageSize);
		
		Page<ExpenseEntity> pageWrapper = expenseService.getExpenses(query);
		
		model.addAttribute("expensesList", pageWrapper.getContent());
		model.addAttribute("pageCount", pageWrapper.getTotalPages());
		model.addAttribute("currentPage", pageWrapper.getNumber());
		
		return "expensesPage";
	}
	
	@GetMapping("/expenses/report")
	public String showReportPage(
			@ModelAttribute("expenseReportQuery") @Valid ExpenseReportQuery query,
			BindingResult result,
			Authentication authentication,
			Model model) throws Exception {
		
		model.addAttribute("expenseCreateCommand", new ExpenseCreateCommand());
		model.addAttribute("expenseFilterQuery", new ExpenseFilterQuery());
		
		validateStartAndEndDate(query.getReportStartDate(), query.getReportEndDate(), result);
		if (result.hasErrors()) {
			return "reportPage";
		}
		
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		query.setUsername(username);
		
		ExpenseReportDto report = expenseService.generateReport(query);
		model.addAttribute("report", report);
		
		return "reportPage";
	}
	
	@GetMapping("/expenses/report/print")
	public ResponseEntity<byte[]> printPdfReport(
			@ModelAttribute("expenseReportQuery") @Valid ExpenseReportQuery query,
			BindingResult result,
			Authentication authentication) {
		
		validateStartAndEndDate(query.getReportStartDate(), query.getReportEndDate(), result);
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}
		
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		query.setUsername(username);
		
		try {
			byte[] pdfReport = expenseService.generatePdfReport(query);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.set("Content-Disposition", "attachment; filename=report.pdf");
			return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	private void validateStartAndEndDate(LocalDate startDate, LocalDate endDate, BindingResult result) {		
		if (startDate != null && endDate != null) {
			if (startDate.isAfter(endDate)) {
				result.rejectValue("reportEndDate", "expenseReportQuery.date.mismatch");
			}
		}
	}
}
