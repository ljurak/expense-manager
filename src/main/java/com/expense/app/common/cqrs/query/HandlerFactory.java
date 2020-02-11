package com.expense.app.common.cqrs.query;

public class HandlerFactory {
	
	public <T extends Query<R>, R extends QueryResult> QueryHandler<T, R> getHandler(T query) {
		return null;
	}
}
