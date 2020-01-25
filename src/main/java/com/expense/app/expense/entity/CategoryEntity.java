package com.expense.app.expense.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class CategoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String name;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private CategoryEntity category = new CategoryEntity();
		
		private Builder() {
		}
		
		public Builder name(String name) {
			category.name = name;
			return this;
		}
		
		public CategoryEntity build() {
			return category;
		}
	}
}
