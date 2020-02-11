package com.expense.app.expense.dto.command;

import com.expense.app.common.cqrs.command.Command;

public class ExpenseDeleteCommand implements Command {
	
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
