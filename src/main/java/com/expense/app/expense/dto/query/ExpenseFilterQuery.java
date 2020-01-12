package com.expense.app.expense.dto.query;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

public class ExpenseFilterQuery {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent(message = "{validation.pastOrPresent}")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent(message = "{validation.pastOrPresent}")
	private LocalDate endDate;
	
	private Long searchCategoryId;
	
	@Digits(integer = 6, fraction = 2, message = "{validation.monetaryValue}")
	private BigDecimal minValue;
	
	@Digits(integer = 6, fraction = 2, message = "{validation.monetaryValue}")
	private BigDecimal maxValue;
	
	private String username;

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Long getSearchCategoryId() {
		return searchCategoryId;
	}

	public void setSearchCategoryId(Long searchCategoryId) {
		this.searchCategoryId = searchCategoryId;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "ExpenseFilterQuery ["
				+ "startDate=" + startDate 
				+ ", endDate=" + endDate 
				+ ", searchCategoryId="	+ searchCategoryId 
				+ ", minValue=" + minValue 
				+ ", maxValue=" + maxValue 
				+ ", username=" + username + "]";
	}
}
