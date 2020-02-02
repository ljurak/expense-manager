package com.expense.app.user.dto.command;

public class ResetPasswordTokenDeleteCommand {
	
	private long tokenId;
	
	public ResetPasswordTokenDeleteCommand(long tokenId) {
		this.tokenId = tokenId;
	}

	public long getTokenId() {
		return tokenId;
	}
}
