package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.BooleanMethod;

public class BannedMethod extends BooleanMethod {

	public BannedMethod(Message message) {
		super(message);
	}

	@Override
	protected Boolean getBoolean(OfflinePlayer player, String event) {
		return player.isBanned();
	}

}
