package com.expense.app.user.exception;

public class ResetPasswordTokenException extends RuntimeException {
	
	private static final long serialVersionUID = -1481296638295785425L;
	
	private long tokenId;

	public ResetPasswordTokenException(String message) {
		super(message);
	}
	
	public ResetPasswordTokenException(String message, long tokenId) {
		super(message);
		this.tokenId = tokenId;
	}

	public long getTokenId() {
		return tokenId;
	}	
}
