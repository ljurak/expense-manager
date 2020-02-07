package com.expense.app.expense.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.expense.app.expense.dto.ExpenseReportDto;
import com.expense.app.expense.dto.query.ExpenseReportQuery;
import com.expense.app.expense.entity.CategoryEntity;
import com.expense.app.expense.entity.ExpenseEntity;
import com.expense.app.expense.repo.ExpenseRepo;

@ExtendWith(MockitoExtension.class)
public class ExpenseQueryServiceImplTest {
	
	private ExpenseRepo expenseRepo;
	
	private ExpenseQueryService expenseService;
	
	@BeforeEach
	public void setUp() {
		expenseRepo = mock(ExpenseRepo.class);
		expenseService = new ExpenseQueryServiceImpl(expenseRepo);
	}
	
	@Test
	public void shouldGenerateEmptyExpenseReport() {
		// given
		ExpenseReportQuery query = new ExpenseReportQuery();
		query.setUsername("John");
		prepareEmptyDataSet();		
		
		// when		
		ExpenseReportDto report = expenseService.generateReport(query);
		
		// then
		assertEquals(0, report.getExpenseCount());
		assertNull(report.getMinExpense());
		assertNull(report.getMaxExpense());
		assertNull(report.getAvgExpense());
		assertNull(report.getSumExpense());
	}
	
	@Test
	public void shouldGenerateExpenseReportForSingleCategory() {
		// given
		ExpenseReportQuery query = new ExpenseReportQuery();
		query.setUsername("John");
		prepareDataSetWithSingleCategory();		
		
		// when		
		ExpenseReportDto report = expenseService.generateReport(query);
		
		// then
		assertEquals(4, report.getExpenseCount());
		assertEquals(new BigDecimal("7.50"), report.getMinExpense());
		assertEquals(new BigDecimal("28.00"), report.getMaxExpense());
		assertEquals(new BigDecimal("17.87"), report.getAvgExpense());
		assertEquals(new BigDecimal("71.49"), report.getSumExpense());
		assertEquals(1, report.getExpenseByCategory().size());
		assertTrue(report.getExpenseByCategory().values().containsAll(List.of(new BigDecimal("71.49"))));
	}
	
	@Test
	public void shouldGenerateExpenseReportForMultipleCategories() {
		// given
		ExpenseReportQuery query = new ExpenseReportQuery();
		query.setUsername("John");
		prepareDataSetWithMultipleCategories();		
		
		// when		
		ExpenseReportDto report = expenseService.generateReport(query);
		
		// then
		assertEquals(12, report.getExpenseCount());
		assertEquals(new BigDecimal("7.50"), report.getMinExpense());
		assertEquals(new BigDecimal("150.00"), report.getMaxExpense());
		assertEquals(new BigDecimal("36.78"), report.getAvgExpense());
		assertEquals(new BigDecimal("441.47"), report.getSumExpense());
		assertEquals(4, report.getExpenseByCategory().size());
		assertTrue(report.getExpenseByCategory().values().containsAll(List.of(
				new BigDecimal("71.49"), new BigDecimal("127.49"), new BigDecimal("150.00"), new BigDecimal("92.49"))));
	}
	
	private void prepareEmptyDataSet() {				
		List<ExpenseEntity> expenseList = new ArrayList<>();		
		when(expenseRepo.findAll(any(), ArgumentMatchers.<Sort>any())).thenReturn(expenseList);
	}
	
	private void prepareDataSetWithSingleCategory() {
		CategoryEntity category1 = CategoryEntity.builder().name("food & drink").build();
				
		List<ExpenseEntity> expenseList = new ArrayList<>();
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("12.50")).category(category1).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("7.50")).category(category1).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("23.49")).category(category1).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("28.00")).category(category1).build());
		
		when(expenseRepo.findAll(any(), ArgumentMatchers.<Sort>any())).thenReturn(expenseList);
	}
	
	private void prepareDataSetWithMultipleCategories() {
		CategoryEntity category1 = CategoryEntity.builder().name("food & drink").build();
		CategoryEntity category2 = CategoryEntity.builder().name("education").build();
		CategoryEntity category3 = CategoryEntity.builder().name("housing").build();
		CategoryEntity category4 = CategoryEntity.builder().name("sport").build();
		
		List<ExpenseEntity> expenseList = new ArrayList<>();
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("12.50")).category(category1).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("7.50")).category(category1).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("23.49")).category(category1).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("28.00")).category(category1).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("79.00")).category(category2).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("18.50")).category(category2).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("29.99")).category(category2).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("150.00")).category(category3).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("35.00")).category(category4).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("8.99")).category(category4).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("17.50")).category(category4).build());
		expenseList.add(ExpenseEntity.builder().value(new BigDecimal("31.00")).category(category4).build());
		
		when(expenseRepo.findAll(any(), ArgumentMatchers.<Sort>any())).thenReturn(expenseList);
	}
}
