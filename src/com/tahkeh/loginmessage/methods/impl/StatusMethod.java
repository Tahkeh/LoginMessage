package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.CaseMethod;

public class StatusMethod extends CaseMethod {

	private final Message message;

	public StatusMethod(final Message message) {
		this.message = message;
	}

	@Override
	protected String call(OfflinePlayer player, String event) {
		return this.message.getStatus(player);
	}

}
