package com.expense.app.expense.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
	public ExpenseReportDto generateReport(ExpenseReportQuery query) {
		Specification<ExpenseEntity> specification = new ExpenseReportSpecification(query);
		List<ExpenseEntity> expenseList = expenseRepo.findAll(specification);
		
		Integer expenseCount = expenseList.size();
		
		if (expenseCount == 0) {
			ExpenseReportDto report = ExpenseReportDto.builder()
					.expenseCount(0)
					.build();
			return report;
		}
		
		BigDecimal minExpense = expenseList.stream()
				.map(ExpenseEntity::getValue)
				.min(Comparator.naturalOrder())
				.orElse(BigDecimal.ZERO);
		
		BigDecimal maxExpense = expenseList.stream()
				.map(ExpenseEntity::getValue)
				.max(Comparator.naturalOrder())
				.orElse(BigDecimal.ZERO);
		
		BigDecimal sumExpense = expenseList.stream()
				.map(ExpenseEntity::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		BigDecimal avgExpense = sumExpense.divide(new BigDecimal(expenseCount), 2, RoundingMode.DOWN);
		
		Map<CategoryEntity, BigDecimal> expenseByCategory = expenseList.stream()
				.collect(Collectors.toMap(
						ExpenseEntity::getCategory, 
						ExpenseEntity::getValue, 
						(oldValue, newValue) -> oldValue.add(newValue)));
		
		return ExpenseReportDto.builder()
				.expenseCount(expenseCount)
				.minExpense(minExpense)
				.maxExpense(maxExpense)
				.avgExpense(avgExpense)
				.sumExpense(sumExpense)
				.expenseByCategory(expenseByCategory)
				.build();
	}	
}
