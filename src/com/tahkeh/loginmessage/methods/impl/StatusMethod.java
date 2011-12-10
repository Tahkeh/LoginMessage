package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.CaseMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class StatusMethod extends CaseMethod {

	private final Message message;

	public StatusMethod(final Message message) {
		super("status");
		this.message = message;
	}

	@Override
	protected String call(OfflinePlayer player, Variables globalParameters) {
		return this.message.getStatus(player);
	}

}
