package com.expense.app.expense.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.app.expense.dto.command.ExpenseCreateCommand;
import com.expense.app.expense.dto.command.ExpenseDeleteCommand;
import com.expense.app.expense.entity.CategoryEntity;
import com.expense.app.expense.entity.ExpenseEntity;
import com.expense.app.expense.exception.CategoryNotFoundException;
import com.expense.app.expense.repo.CategoryRepo;
import com.expense.app.expense.repo.ExpenseRepo;
import com.expense.app.user.entity.UserEntity;
import com.expense.app.user.exception.UserNotFoundException;
import com.expense.app.user.repo.UserRepo;

@Service
@Transactional
public class ExpenseComandServiceImpl implements ExpenseCommandService {
	
	private ExpenseRepo expenseRepo;
	
	private CategoryRepo categoryRepo;
	
	private UserRepo userRepo;
	
	public ExpenseComandServiceImpl(ExpenseRepo expenseRepo, CategoryRepo categoryRepo, UserRepo userRepo) {
		this.expenseRepo = expenseRepo;
		this.categoryRepo = categoryRepo;
		this.userRepo = userRepo;
	}
	
	@Override
	public void createExpense(ExpenseCreateCommand command) {
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
	
	@Override
	public void deleteExpense(ExpenseDeleteCommand command) {
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
