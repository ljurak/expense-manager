package com.expense.app.common.cqrs.query;

public class QueryDispatcherImpl implements QueryDispatcher {
	
	private HandlerFactory handlerFactory;

	public QueryDispatcherImpl(HandlerFactory handlerFactory) {
		this.handlerFactory = handlerFactory;
	}

	@Override
	public <T extends Query<R>, R extends QueryResult> R dispatch(T query) {
		QueryHandler<T, R> handler = handlerFactory.getHandler(query);
		return handler.handle(query);
	}
}
