package com.expense.app.common.cqrs.command;

public interface CommandHandler<T extends Command> {
	
	void handle(T command);
}
