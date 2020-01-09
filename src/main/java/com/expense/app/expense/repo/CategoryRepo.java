package com.expense.app.expense.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.expense.app.expense.entity.CategoryEntity;

@Repository
public interface CategoryRepo extends CrudRepository<CategoryEntity, Long> {

}
