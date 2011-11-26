package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public class LastLoginMethod extends OriginalMethod {

	private final Message message;

	public LastLoginMethod(final Message message) {
		this.message = message;
	}

	@Override
	protected String call(OfflinePlayer player, String event, DefaultVariables globalParameters) {
		return this.message.getTimeDifference(this.message.getLastLogin(player.getName()));
	}

}
