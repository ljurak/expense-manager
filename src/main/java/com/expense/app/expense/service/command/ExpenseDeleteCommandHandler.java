package com.expense.app.expense.service.command;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.common.cqrs.command.CommandHandler;
import com.expense.app.expense.dto.command.ExpenseDeleteCommand;
import com.expense.app.expense.entity.ExpenseEntity;
import com.expense.app.expense.repo.ExpenseRepo;

@Service
public class ExpenseDeleteCommandHandler implements CommandHandler<ExpenseDeleteCommand> {
	
	private ExpenseRepo expenseRepo;
	
	public ExpenseDeleteCommandHandler(ExpenseRepo expenseRepo) {
		this.expenseRepo = expenseRepo;
	}

	@Override
	@Transactional
	public void handle(ExpenseDeleteCommand command) {
		ExpenseEntity expense = expenseRepo.findById(command.getId()).orElse(null);
		
		if (expense != null) {
			String username = expense.getUser().getUsername();
			if (!username.equals(command.getUsername())) {
				throw new AccessDeniedException(null);
			}
			
			expenseRepo.deleteById(command.getId());
		}
	}
}
