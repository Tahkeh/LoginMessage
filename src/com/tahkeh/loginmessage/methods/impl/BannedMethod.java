package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class BannedMethod extends BooleanMethod {

	public BannedMethod(Message message) {
		super(message, "banned");
	}

	@Override
	protected Boolean getBoolean(OfflinePlayer player, Variables globalParameters) {
		return player.isBanned();
	}

}
