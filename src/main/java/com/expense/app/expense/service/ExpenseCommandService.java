package com.expense.app.expense.service;

import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.dto.command.ExpenseDeleteCommand;

public interface ExpenseCommandService {
	void createExpense(ExpenseCreateCommand command);
	void deleteExpense(ExpenseDeleteCommand command);
}
