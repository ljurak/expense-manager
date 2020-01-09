package com.expense.app.expense.dto.command;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class ExpenseCreateCommand {
	
	@NotNull(message = "{validation.notNull}")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent(message = "{validation.pastOrPresent}")
	private LocalDate date;
	
	@NotNull(message = "{validation.required}")
	@Digits(integer = 6, fraction = 2, message = "{validation.monetaryValue}")
	private BigDecimal value;
	
	@NotNull(message = "{validation.required}")
	private Long categoryId;
	
	private String username;
	
	@Size(max = 255, message = "{validation.maxSize}")
	private String description;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ExpenseCreateCommand ["
				+ "date=" + date 
				+ ", value=" + value 
				+ ", categoryId=" + categoryId 
				+ ", username=" + username
				+ ", description=" + description + "]";
	}
}
