package com.expense.app.expense.exception;

public class CategoryNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 6644477180827093920L;

	public CategoryNotFoundException(String message) {
		super(message);
	}
}
