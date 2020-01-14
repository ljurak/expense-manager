package com.expense.app.expense.dto.query;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

public class ExpenseReportQuery {
	
	@NotNull(message = "{validation.required}")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent(message = "{validation.pastOrPresent}")
	private LocalDate reportStartDate;
	
	@NotNull(message = "{validation.required}")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent(message = "{validation.pastOrPresent}")
	private LocalDate reportEndDate;
	
	private Long reportCategoryId;
	
	private String username;

	public LocalDate getReportStartDate() {
		return reportStartDate;
	}

	public void setReportStartDate(LocalDate reportStartDate) {
		this.reportStartDate = reportStartDate;
	}

	public LocalDate getReportEndDate() {
		return reportEndDate;
	}

	public void setReportEndDate(LocalDate reportEndDate) {
		this.reportEndDate = reportEndDate;
	}

	public Long getReportCategoryId() {
		return reportCategoryId;
	}

	public void setReportCategoryId(Long reportCategoryId) {
		this.reportCategoryId = reportCategoryId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "ExpenseReportQuery ["
				+ "reportStartDate=" + reportStartDate 
				+ ", reportEndDate=" + reportEndDate 
				+ ", reportCategoryId=" + reportCategoryId
				+ ", username=" + username + "]";
	}	
}
