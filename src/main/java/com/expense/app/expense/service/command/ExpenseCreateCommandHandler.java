package com.expense.app.expense.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.common.cqrs.command.CommandHandler;
import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.entity.CategoryEntity;
import com.expense.app.expense.entity.ExpenseEntity;
import com.expense.app.expense.exception.CategoryNotFoundException;
import com.expense.app.expense.repo.CategoryRepo;
import com.expense.app.expense.repo.ExpenseRepo;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.exception.UserNotFoundException;
import com.expense.app.user.repo.UserRepo;

@Service
public class ExpenseCreateCommandHandler implements CommandHandler<ExpenseCreateCommand> {
	
	private ExpenseRepo expenseRepo;
	
	private CategoryRepo categoryRepo;
	
	private UserRepo userRepo;
	
	public ExpenseCreateCommandHandler(ExpenseRepo expenseRepo, CategoryRepo categoryRepo, UserRepo userRepo) {
		this.expenseRepo = expenseRepo;
		this.categoryRepo = categoryRepo;
		this.userRepo = userRepo;
	}

	@Override
	@Transactional
	public void handle(ExpenseCreateCommand command) {
		if (command.getDescription().length() == 0) {
			command.setDescription(null);
		}
		
		CategoryEntity category = categoryRepo.findById(command.getCategoryId())
				.orElseThrow(() -> new CategoryNotFoundException("Category with id [" + command.getCategoryId() + "] does not exist"));
		
		UserEntity user = userRepo.findByUsername(command.getUsername())
				.orElseThrow(() -> new UserNotFoundException("User [" + command.getUsername() + "] does not exist"));
		
		ExpenseEntity expense = ExpenseEntity.builder()
				.date(command.getDate())
				.value(command.getValue())
				.category(category)
				.user(user)
				.description(command.getDescription())
				.build();
		
		expenseRepo.save(expense);		
	}
}
