package com.expense.app.expense.dto.command;

public class ExpenseDeleteCommand {
	
	private long id;
	
	private String username;
	
	public ExpenseDeleteCommand() {
	}

	public ExpenseDeleteCommand(long id, String username) {
		this.id = id;
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
