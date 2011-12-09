package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.BooleanMethod;

public class WhitelistedMethod extends BooleanMethod {

	public WhitelistedMethod(Message message) {
		super(message, "white");
	}

	@Override
	protected Boolean getBoolean(OfflinePlayer player, String event) {
		return player.isWhitelisted();
	}

}
