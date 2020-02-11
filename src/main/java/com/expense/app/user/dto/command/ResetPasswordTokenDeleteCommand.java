package com.expense.app.user.dto.command;

import com.expense.app.common.cqrs.command.Command;

public class ResetPasswordTokenDeleteCommand implements Command {
	
	private long tokenId;
	
	public ResetPasswordTokenDeleteCommand(long tokenId) {
		this.tokenId = tokenId;
	}

	public long getTokenId() {
		return tokenId;
	}
}
