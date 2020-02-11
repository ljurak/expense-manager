package com.expense.app.expense.dto.query;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

public class ExpenseFilterQuery {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent(message = "{validation.pastOrPresent}")
	private LocalDate searchStartDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent(message = "{validation.pastOrPresent}")
	private LocalDate searchEndDate;
	
	private Long searchCategoryId;
	
	@Digits(integer = 6, fraction = 2, message = "{validation.monetaryValue}")
	private BigDecimal minValue;
	
	@Digits(integer = 6, fraction = 2, message = "{validation.monetaryValue}")
	private BigDecimal maxValue;
	
	private String username;
	
	private int page;
	
	private int pageSize;

	public LocalDate getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate(LocalDate searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public LocalDate getSearchEndDate() {
		return searchEndDate;
	}

	public void setSearchEndDate(LocalDate searchEndDate) {
		this.searchEndDate = searchEndDate;
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "ExpenseFilterQuery ["
				+ "searchStartDate=" + searchStartDate 
				+ ", searchEndDate=" + searchEndDate 
				+ ", searchCategoryId="	+ searchCategoryId 
				+ ", minValue=" + minValue 
				+ ", maxValue=" + maxValue 
				+ ", username=" + username + "]";
	}
}
