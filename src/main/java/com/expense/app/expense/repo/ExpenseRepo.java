package com.expense.app.expense.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expense.app.expense.entity.ExpenseEntity;

@Repository
public interface ExpenseRepo extends CrudRepository<ExpenseEntity, Long> {
	
	@Query("select e from ExpenseEntity e where e.user.username = :username order by e.date desc")
	List<ExpenseEntity> findByUsername(@Param("username") String username);
}
