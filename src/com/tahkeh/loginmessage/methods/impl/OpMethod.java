package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.BooleanMethod;

public class OpMethod extends BooleanMethod {

	public OpMethod(Message message) {
		super(message, "op");
	}

	@Override
	protected Boolean getBoolean(OfflinePlayer player, String event) {
		return player.isOp();
	}

}
