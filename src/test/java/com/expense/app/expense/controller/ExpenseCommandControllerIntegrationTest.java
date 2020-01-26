package com.expense.app.expense.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseCommandControllerIntegrationTest {
	
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
	@WithUserDetails("ananas")
	public void shouldFailValidationWhenMissingParameters() throws Exception {
		// when
		mockMvc.perform(post("/expenses/create")
				.param("date", "2019-12-08")
				.param("description", "")
				.with(csrf()))
		
		// then
		.andExpect(status().isOk())
		.andExpect(view().name("expensesPage"))
		.andExpect(model().attributeExists("expenseCategories"))
		.andExpect(model().attributeExists("expenseFilterQuery"))
		.andExpect(model().attributeExists("expenseReportQuery"))
		.andExpect(model().attributeDoesNotExist("expensesList"));
	}
	
	@Test
	@WithUserDetails("ananas")
	public void shouldCreateExpense() throws Exception {
		// when
		mockMvc.perform(post("/expenses/create")
				.param("date", "2019-12-08")
				.param("value", "23.56")
				.param("categoryId", "4")
				.param("description", "")
				.with(csrf()))
		
		// then
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/expenses/show"))
		.andExpect(view().name("redirect:/expenses/show"));
	}
}
