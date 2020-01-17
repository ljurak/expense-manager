package com.expense.app.expense.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.expense.app.expense.dto.query.ExpenseFilterQuery;
import com.expense.app.expense.entity.ExpenseEntity;

public class ExpenseFilterSpecification implements Specification<ExpenseEntity> {
	
	private static final long serialVersionUID = -2848929613924124828L;
	
	private final ExpenseFilterQuery filters;
	
	public ExpenseFilterSpecification(ExpenseFilterQuery filters) {
		this.filters = filters;
	}
	
	@Override
	public Predicate toPredicate(Root<ExpenseEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		
		predicates.add(criteriaBuilder.equal(root.get("user").get("username"), filters.getUsername()));
		if (filters.getSearchStartDate() != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filters.getSearchStartDate()));
		}
		if (filters.getSearchEndDate() != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filters.getSearchEndDate()));
		}
		if (filters.getSearchCategoryId() != null) {
			predicates.add(criteriaBuilder.equal(root.get("category").get("id"), filters.getSearchCategoryId()));
		}
		if (filters.getMinValue() != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("value"), filters.getMinValue()));
		}
		if (filters.getMaxValue() != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("value"), filters.getMaxValue()));
		}
		
		Predicate predicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		return predicate;
	}	
}
