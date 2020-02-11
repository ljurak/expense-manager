package com.expense.app.common.cqrs.query;

public interface QueryDispatcher {
	
	<T extends Query<R>, R extends QueryResult> R dispatch(T query);
}
