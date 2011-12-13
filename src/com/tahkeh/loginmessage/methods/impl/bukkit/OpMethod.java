package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class OpMethod extends BooleanMethod<BukkitVariables> {

	public OpMethod(Message message) {
		super(message, "op");
	}

	@Override
	protected Boolean getBoolean(BukkitVariables globalParameters) {
		return globalParameters.offlinePlayer.isOp();
	}

}
