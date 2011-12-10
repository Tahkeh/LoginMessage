package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class OpMethod extends BooleanMethod {

	public OpMethod(Message message) {
		super(message, "op");
	}

	@Override
	protected Boolean getBoolean(OfflinePlayer player, Variables globalParameters) {
		return player.isOp();
	}

}
