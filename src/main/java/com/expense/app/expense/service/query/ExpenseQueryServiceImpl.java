package com.expense.app.expense.service.query;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.common.mail.service.EmailService;
import com.expense.app.expense.dto.query.ExpenseFilterQuery;
import com.expense.app.expense.dto.query.ExpenseReportQuery;
import com.expense.app.expense.dto.result.ExpenseReportDto;
import com.expense.app.expense.entity.CategoryEntity;
import com.expense.app.expense.entity.ExpenseEntity;
import com.expense.app.expense.repo.CategoryRepo;
import com.expense.app.expense.repo.ExpenseFilterSpecification;
import com.expense.app.expense.repo.ExpenseRepo;
import com.expense.app.expense.repo.ExpenseReportSpecification;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.exception.UserNotFoundException;
import com.expense.app.user.repo.UserRepo;

@Service
@Transactional(readOnly = true)
public class ExpenseQueryServiceImpl implements ExpenseQueryService {
	
	private ExpenseRepo expenseRepo;
	
	private CategoryRepo categoryRepo;
	
	private UserRepo userRepo;
	
	private ExpenseReportCreator reportCreator;
	
	private EmailService emailService;
	
	public ExpenseQueryServiceImpl(
			ExpenseRepo expenseRepo, 
			CategoryRepo categoryRepo,
			UserRepo userRepo,
			ExpenseReportCreator reportCreator,
			EmailService emailService) {
		this.expenseRepo = expenseRepo;
		this.categoryRepo = categoryRepo;
		this.userRepo = userRepo;
		this.reportCreator = reportCreator;
		this.emailService = emailService;
	}
	
	@Override
	public Iterable<CategoryEntity> getCategories() {
		return categoryRepo.findAll();
	}
	
	@Override
	public Page<ExpenseEntity> getExpenses(ExpenseFilterQuery query) {
		Specification<ExpenseEntity> specification = new ExpenseFilterSpecification(query);
		return expenseRepo.findAll(
				specification, PageRequest.of(query.getPage(), query.getPageSize(), Sort.by("date").descending()));
	}

	@Override
	public ExpenseReportDto generateReport(ExpenseReportQuery query) {
		Specification<ExpenseEntity> specification = new ExpenseReportSpecification(query);
		List<ExpenseEntity> expenseList = expenseRepo.findAll(specification, Sort.by("date").ascending());
			
		if (expenseList.isEmpty()) {
			ExpenseReportDto report = ExpenseReportDto.builder()
					.expenseCount(0)
					.build();
			return report;
		}
		
		Integer expenseCount = expenseList.size();			
		BigDecimal minExpense = calculateMinExpense(expenseList);		
		BigDecimal maxExpense = calculateMaxExpense(expenseList);		
		BigDecimal sumExpense = calculateSumExpense(expenseList);		
		BigDecimal avgExpense = sumExpense.divide(new BigDecimal(expenseCount), 2, RoundingMode.DOWN);		
		Map<CategoryEntity, BigDecimal> expenseByCategory = calculateExpenseByCategory(expenseList);
		
		return ExpenseReportDto.builder()
				.startDate(query.getReportStartDate())
				.endDate(query.getReportEndDate())
				.expenseCount(expenseCount)
				.minExpense(minExpense)
				.maxExpense(maxExpense)
				.avgExpense(avgExpense)
				.sumExpense(sumExpense)
				.expenseList(expenseList)
				.expenseByCategory(expenseByCategory)
				.build();
	}
	
	@Override
	public byte[] generatePdfReport(ExpenseReportQuery query) throws Exception {
		ExpenseReportDto report = generateReport(query);
		return reportCreator.generatePdfReport(report);
	}
	
	@Override
	public ExpenseReportDto sendPdfReport(ExpenseReportQuery query) throws Exception {
		ExpenseReportDto report = generateReport(query);
		byte[] reportPdf = reportCreator.generatePdfReport(report);
		UserEntity user = userRepo.findByUsername(query.getUsername())
				.orElseThrow(() -> new UserNotFoundException("User [" + query.getUsername() + "] does not exist."));
		emailService.sendPdfReportEmail(user.getEmail(), reportPdf);
		return report;
	}

	private BigDecimal calculateMinExpense(List<ExpenseEntity> expenseList) {
		return expenseList.stream()
				.map(ExpenseEntity::getValue)
				.min(Comparator.naturalOrder())
				.orElse(BigDecimal.ZERO);
	}
	
	private BigDecimal calculateMaxExpense(List<ExpenseEntity> expenseList) {
		return expenseList.stream()
				.map(ExpenseEntity::getValue)
				.max(Comparator.naturalOrder())
				.orElse(BigDecimal.ZERO);
	}
	
	private BigDecimal calculateSumExpense(List<ExpenseEntity> expenseList) {
		return expenseList.stream()
				.map(ExpenseEntity::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	private Map<CategoryEntity, BigDecimal> calculateExpenseByCategory(List<ExpenseEntity> expenseList) {
		return expenseList.stream()
				.collect(Collectors.toMap(
						ExpenseEntity::getCategory, 
						ExpenseEntity::getValue, 
						(oldValue, newValue) -> oldValue.add(newValue)));
	}
}
