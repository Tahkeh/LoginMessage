package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class OnlineMethod extends BooleanMethod {

	public OnlineMethod(final Message message) {
		super(message, "online");
	}

	@Override
	protected Boolean getBoolean(OfflinePlayer player, Variables globalParameters) {
		return player.isOnline();
	}
}
