package com.expense.app.expense.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.expense.dto.ExpenseReportDto;
import com.expense.app.expense.dto.query.ExpenseReportQuery;
import com.expense.app.expense.entity.CategoryEntity;
import com.expense.app.expense.entity.ExpenseEntity;
import com.expense.app.expense.repo.ExpenseRepo;
import com.expense.app.expense.repo.ExpenseReportSpecification;

@Service
public class ExpenseQueryServiceImpl implements ExpenseQueryService {
	
	private ExpenseRepo expenseRepo;
	
	public ExpenseQueryServiceImpl(ExpenseRepo expenseRepo) {
		this.expenseRepo = expenseRepo;
	}

	@Override
	@Transactional(readOnly = true)
	public ExpenseReportDto generateReport(ExpenseReportQuery query) {
		Specification<ExpenseEntity> specification = new ExpenseReportSpecification(query);
		List<ExpenseEntity> expenseList = expenseRepo.findAll(specification);
			
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
				.expenseCount(expenseCount)
				.minExpense(minExpense)
				.maxExpense(maxExpense)
				.avgExpense(avgExpense)
				.sumExpense(sumExpense)
				.expenseByCategory(expenseByCategory)
				.build();
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
