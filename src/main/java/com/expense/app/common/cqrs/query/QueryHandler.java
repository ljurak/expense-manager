package com.expense.app.common.cqrs.query;

public interface QueryHandler<T extends Query<R>, R extends QueryResult> {
	
	R handle(T query);
}
