package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class OnlineMethod extends BooleanMethod<BukkitVariables> {

	public OnlineMethod(final Message message) {
		super(message, "online");
	}

	@Override
	protected Boolean getBoolean(BukkitVariables globalParameters) {
		return globalParameters.offlinePlayer.isOnline();
	}
}
