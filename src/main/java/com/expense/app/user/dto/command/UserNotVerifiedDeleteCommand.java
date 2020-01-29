package com.expense.app.user.dto.command;

public class UserNotVerifiedDeleteCommand {
	
	private long tokenId;

	public UserNotVerifiedDeleteCommand(long tokenId) {
		this.tokenId = tokenId;
	}

	public long getTokenId() {
		return tokenId;
	}
}
