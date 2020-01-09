package com.expense.app.expense.service;

import com.expense.app.expense.dto.command.ExpenseCreateCommand;

public interface ExpenseCommandService {
	void createExpense(ExpenseCreateCommand command);
}
