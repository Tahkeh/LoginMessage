package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class BannedMethod extends BooleanMethod<BukkitVariables> {

	public BannedMethod(Message message) {
		super(message, "banned");
	}

	@Override
	protected Boolean getBoolean(BukkitVariables globalParameters) {
		return globalParameters.offlinePlayer.isBanned();
	}

}
