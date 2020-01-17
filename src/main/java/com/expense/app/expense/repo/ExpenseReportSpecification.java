package com.expense.app.expense.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.expense.app.expense.dto.query.ExpenseReportQuery;
import com.expense.app.expense.entity.ExpenseEntity;

public class ExpenseReportSpecification implements Specification<ExpenseEntity> {
	
	private static final long serialVersionUID = -488785877197629521L;
	
	private final ExpenseReportQuery reportQuery;
		
	public ExpenseReportSpecification(ExpenseReportQuery reportQuery) {
		this.reportQuery = reportQuery;
	}

	@Override
	public Predicate toPredicate(Root<ExpenseEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		
		predicates.add(criteriaBuilder.equal(root.get("user").get("username"), reportQuery.getUsername()));
		if (reportQuery.getReportStartDate() != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), reportQuery.getReportStartDate()));
		}
		if (reportQuery.getReportEndDate() != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), reportQuery.getReportEndDate()));
		}
		if (reportQuery.getReportCategoryId() != null) {
			predicates.add(criteriaBuilder.equal(root.get("category").get("id"), reportQuery.getReportCategoryId()));
		}
		
		Predicate predicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		return predicate;
	}
}
