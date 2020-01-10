package com.expense.app.expense.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expense.app.expense.entity.ExpenseEntity;

@Repository
public interface ExpenseRepo extends CrudRepository<ExpenseEntity, Long> {
	
	@Query("select e from ExpenseEntity e where e.user.username = :username")
	Page<ExpenseEntity> findByUsername(@Param("username") String username, Pageable pageable);
}
