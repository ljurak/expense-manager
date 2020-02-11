package com.expense.app.common.cqrs.command;

import org.springframework.stereotype.Component;

@Component
public class CommandDispatcherImpl implements CommandDispatcher {
	
	private HandlerFactory handlerFactory;	
	
	public CommandDispatcherImpl(HandlerFactory handlerFactory) {
		this.handlerFactory = handlerFactory;
	}

	@Override
	public <T extends Command> void dispatch(T command) {
		CommandHandler<T> handler = handlerFactory.getHandler(command);
		handler.handle(command);
	}	
}
