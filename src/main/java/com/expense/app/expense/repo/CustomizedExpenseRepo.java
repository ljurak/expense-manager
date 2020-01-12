package com.expense.app.expense.repo;

import org.springframework.data.domain.Pageable;

import com.expense.app.expense.dto.query.ExpenseFilterQuery;
import com.expense.app.expense.entity.ExpenseEntity;

public interface CustomizedExpenseRepo {
	PageWrapper<ExpenseEntity> findAllByFilter(ExpenseFilterQuery query, Pageable pageable);
}
