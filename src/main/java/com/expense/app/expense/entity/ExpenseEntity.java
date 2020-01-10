package com.expense.app.expense.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.expense.app.user.entity.UserEntity;

@Entity
@Table(name = "expenses")
public class ExpenseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private LocalDate date;
	
	@Column(nullable = false, precision = 8, scale = 2)
	private BigDecimal value;
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private CategoryEntity category;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
	
	@Column(nullable = true, length = 255)
	private String description;

	public Long getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public BigDecimal getValue() {
		return value;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public UserEntity getUser() {
		return user;
	}

	public String getDescription() {
		return description;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private ExpenseEntity expense = new ExpenseEntity();
		
		private Builder() {
		}
		
		public Builder date(LocalDate date) {
			expense.date = date;
			return this;
		}
		
		public Builder value(BigDecimal value) {
			expense.value = value;
			return this;
		}
		
		public Builder category(CategoryEntity category) {
			expense.category = category;
			return this;
		}
		
		public Builder user(UserEntity user) {
			expense.user = user;
			return this;
		}
		
		public Builder description(String description) {
			expense.description = description;
			return this;
		}
		
		public ExpenseEntity build() {
			return expense;
		}
	}
}
