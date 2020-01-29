package com.expense.app.user.exception;

public class VerificationTokenException extends RuntimeException {
	
	private static final long serialVersionUID = 7267045920181578448L;
	
	private long tokenId;
	
	public VerificationTokenException(String message) {
		super(message);
	}

	public VerificationTokenException(String message, long tokenId) {
		super(message);
		this.tokenId = tokenId;
	}
	
	public long getTokenId() {
		return tokenId;
	}
}
