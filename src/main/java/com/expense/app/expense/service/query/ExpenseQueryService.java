package com.expense.app.expense.service.query;

import org.springframework.data.domain.Page;

import com.expense.app.expense.dto.query.ExpenseFilterQuery;
import com.expense.app.expense.dto.query.ExpenseReportQuery;
import com.expense.app.expense.dto.result.ExpenseReportDto;
import com.expense.app.expense.entity.CategoryEntity;
import com.expense.app.expense.entity.ExpenseEntity;

public interface ExpenseQueryService {
	Iterable<CategoryEntity> getCategories();
	Page<ExpenseEntity> getExpenses(ExpenseFilterQuery query);
	ExpenseReportDto generateReport(ExpenseReportQuery query);
	byte[] generatePdfReport(ExpenseReportQuery query) throws Exception;
	ExpenseReportDto sendPdfReport(ExpenseReportQuery query) throws Exception;
}
