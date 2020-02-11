package com.expense.app.expense.service;

import com.expense.app.expense.dto.query.ExpenseReportQuery;
import com.expense.app.expense.dto.result.ExpenseReportDto;

public interface ExpenseQueryService {
	ExpenseReportDto generateReport(ExpenseReportQuery query);
}
