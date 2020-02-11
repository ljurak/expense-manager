package com.expense.app.common.cqrs.command;

public interface CommandDispatcher {
	
	<T extends Command> void dispatch(T command);
}
