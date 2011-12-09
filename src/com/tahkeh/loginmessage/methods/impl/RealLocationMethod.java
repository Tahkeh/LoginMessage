package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class RealLocationMethod extends OriginalMethod {

	private final String type;
	private final Message message;

	public RealLocationMethod(final String type, final Message message) {
		super(type);
		this.type = type;
		this.message = message;
	}

	@Override
	protected String call(OfflinePlayer player, String event, Variables globalParameters) {
		return this.message.getLocation(this.type, player.getName(), event);
	}
}
