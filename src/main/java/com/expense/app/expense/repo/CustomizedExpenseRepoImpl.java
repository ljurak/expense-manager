package com.expense.app.expense.repo;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.expense.app.expense.dto.query.ExpenseFilterQuery;
import com.expense.app.expense.entity.ExpenseEntity;

@Repository
public class CustomizedExpenseRepoImpl implements CustomizedExpenseRepo {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public PageWrapper<ExpenseEntity> findAllByFilter(ExpenseFilterQuery filterQuery, Pageable pageable) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ExpenseEntity> criteria = cb.createQuery(ExpenseEntity.class);
		
		Root<ExpenseEntity> root = criteria.from(ExpenseEntity.class);
		
		Predicate predicate = cb.equal(root.get("user").get("username"), filterQuery.getUsername());		
		if (filterQuery.getStartDate() != null) {
			predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("date"), filterQuery.getStartDate()));
		}
		if (filterQuery.getEndDate() != null) {
			predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("date"), filterQuery.getEndDate()));
		}
		if (filterQuery.getSearchCategoryId() != null) {
			predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), filterQuery.getSearchCategoryId()));
		}
		if (filterQuery.getMinValue() != null) {
			predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("value"), filterQuery.getMinValue()));
		}
		if (filterQuery.getMaxValue() != null) {
			predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("value"), filterQuery.getMaxValue()));
		}
		
		List<Order> orderBy = pageable.getSort().get()
				.map(sort -> sort.isAscending() 
						? cb.asc(root.get(sort.getProperty())) 
						: cb.desc(root.get(sort.getProperty())))
				.collect(Collectors.toList());
		
		criteria.select(root).where(predicate).orderBy(orderBy);		
		TypedQuery<ExpenseEntity> query = em.createQuery(criteria);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<ExpenseEntity> content = query.getResultList();
		
		CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
		Root<ExpenseEntity> countRoot = countCriteria.from(ExpenseEntity.class);
		countCriteria.select(cb.count(countRoot)).where(predicate);
		TypedQuery<Long> countQuery = em.createQuery(countCriteria);
		Long countResult = countQuery.getSingleResult();
		
		PageWrapper<ExpenseEntity> page = new PageWrapper<>();
		page.setContent(content);
		page.setCurrentPage(pageable.getPageNumber());
		page.setTotalPages((int) Math.ceil((double) countResult / pageable.getPageSize()));
		
		return page;
	}
}
