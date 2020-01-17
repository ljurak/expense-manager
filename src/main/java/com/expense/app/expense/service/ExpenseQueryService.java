package com.expense.app.expense.service;

import com.expense.app.expense.dto.ExpenseReportDto;
import com.expense.app.expense.dto.query.ExpenseReportQuery;

public interface ExpenseQueryService {
	ExpenseReportDto generateReport(ExpenseReportQuery query);
}
