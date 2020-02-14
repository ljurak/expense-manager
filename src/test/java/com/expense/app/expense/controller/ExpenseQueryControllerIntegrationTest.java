package com.expense.app.expense.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class ExpenseQueryControllerIntegrationTest {
	
	@Autowired
	private WebApplicationContext webContext;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(webContext)
				.apply(springSecurity())
				.build();
	}	
	
	@Test
	@WithMockUser("admin")
	public void shouldFailValidationWhenFilteringExpensesWithInvalidParameters() throws Exception {
		// when
		mockMvc.perform(get("/expenses/show")
				.param("searchStartDate", "18-12-2019")
				.param("minValue", "50.00"))
		
		// then
		.andExpect(status().isOk())
		.andExpect(view().name("expensesPage"))
		.andExpect(model().attributeExists("expenseCategories"))
		.andExpect(model().attributeExists("expenseFilterQuery"))
		.andExpect(model().attributeExists("expenseCreateCommand"))
		.andExpect(model().attributeExists("expenseReportQuery"))
		.andExpect(model().attributeDoesNotExist("expensesList"));
	}
	
	@Test
	@WithMockUser("admin")
	public void shouldShowFilteredExpenses() throws Exception {
		// when
		mockMvc.perform(get("/expenses/show")
				.param("searchStartDate", "2019-06-18")
				.param("minValue", "2.00"))
		
		// then
		.andExpect(status().isOk())
		.andExpect(view().name("expensesPage"))
		.andExpect(model().attributeExists("expensesList"))
		.andExpect(model().attributeExists("pageCount"))
		.andExpect(model().attribute("pageCount", 7));
	}
	
	@Test
	@WithMockUser("admin")
	public void shouldShowReportPage() throws Exception {
		// when
		mockMvc.perform(get("/expenses/report")
				.param("reportStartDate", "2019-06-18")
				.param("reportEndDate", "2019-12-31"))
		
		// then
		.andExpect(status().isOk())
		.andExpect(view().name("reportPage"))
		.andExpect(model().attributeExists("report"));
	}
	
	@Test
	@WithMockUser("admin")
	public void shouldFailValidationWhenGeneratingReportWithInvalidParameters() throws Exception {
		// when
		mockMvc.perform(get("/expenses/report")
				.param("reportStartDate", "18-12-2019")
				.param("reportEndDate", "15-10-2019"))
		
		// then
		.andExpect(status().isOk())
		.andExpect(view().name("reportPage"))
		.andExpect(model().attributeExists("expenseFilterQuery"))
		.andExpect(model().attributeExists("expenseCreateCommand"))
		.andExpect(model().attributeExists("expenseReportQuery"))
		.andExpect(model().attributeDoesNotExist("report"));
	}
	
	@Test
	@WithMockUser("admin")
	public void shouldGeneratePdfReport() throws Exception {
		// when
		mockMvc.perform(get("/expenses/report/print")
				.param("reportStartDate", "2019-06-18")
				.param("reportEndDate", "2019-12-31"))
		
		// then
		.andExpect(status().isOk())
		.andExpect(header().string("Content-Type", "application/pdf"))
		.andExpect(header().exists("Content-Disposition"))
		.andExpect(content().contentType(MediaType.APPLICATION_PDF));
	}
	
	@Test
	@WithMockUser("admin")
	public void shouldNotGeneratePdfReportWhenInvalidParameters() throws Exception {
		// when
		mockMvc.perform(get("/expenses/report/print")
				.param("reportStartDate", "2020-01-25")
				.param("reportEndDate", "2019-12-31"))
		
		// then
		.andExpect(status().isBadRequest());
	}
}
