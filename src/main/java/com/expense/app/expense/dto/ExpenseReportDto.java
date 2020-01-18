package com.expense.app.expense.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.expense.app.expense.entity.CategoryEntity;

public class ExpenseReportDto {
	
	private Integer expenseCount;
	
	private BigDecimal minExpense;
	
	private BigDecimal maxExpense;
	
	private BigDecimal avgExpense;
	
	private BigDecimal sumExpense;
	
	private Map<CategoryEntity, BigDecimal> expenseByCategory = new HashMap<>();

	public Integer getExpenseCount() {
		return expenseCount;
	}
	
	public BigDecimal getMinExpense() {
		return minExpense;
	}

	public BigDecimal getMaxExpense() {
		return maxExpense;
	}

	public BigDecimal getAvgExpense() {
		return avgExpense;
	}

	public BigDecimal getSumExpense() {
		return sumExpense;
	}

	public Map<CategoryEntity, BigDecimal> getExpenseByCategory() {
		return expenseByCategory;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private ExpenseReportDto report = new ExpenseReportDto();
		
		private Builder() {
		}
		
		public Builder expenseCount(Integer expenseCount) {
			report.expenseCount = expenseCount;
			return this;
		}
		
		public Builder minExpense(BigDecimal minExpense) {
			report.minExpense = minExpense;
			return this;
		}
		
		public Builder maxExpense(BigDecimal maxExpense) {
			report.maxExpense = maxExpense;
			return this;
		}
		
		public Builder avgExpense(BigDecimal avgExpense) {
			report.avgExpense = avgExpense;
			return this;
		}
		
		public Builder sumExpense(BigDecimal sumExpense) {
			report.sumExpense = sumExpense;
			return this;
		}
		
		public Builder expenseByCategory(Map<CategoryEntity, BigDecimal> expenseByCategory) {
			report.expenseByCategory = expenseByCategory;
			return this;
		}
		
		public ExpenseReportDto build() {
			return report;
		}
	}
 }
