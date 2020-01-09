package com.expense.app.expense.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.expense.app.expense.entity.ExpenseEntity;

@Repository
public interface ExpenseRepo extends CrudRepository<ExpenseEntity, Long> {

}
