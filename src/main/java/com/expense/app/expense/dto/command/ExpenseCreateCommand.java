package com.expense.app.expense.dto.command;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

public class ExpenseCreateCommand {
	
	@NotNull
	@PastOrPresent
	private LocalDate date;
	
	@NotNull
	@Digits(integer = 6, fraction = 2)
	private BigDecimal value;
	
	@NotBlank
	private String category;
	
	@NotBlank
	private String user;
	
	@Size(max = 255)
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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
				+ ", category=" + category 
				+ ", user=" + user
				+ ", description=" + description + "]";
	}
}
