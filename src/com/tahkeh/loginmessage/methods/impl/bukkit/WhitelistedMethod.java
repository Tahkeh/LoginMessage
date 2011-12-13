package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class WhitelistedMethod extends BooleanMethod<BukkitVariables> {

	public WhitelistedMethod(Message message) {
		super(message, "white");
	}

	@Override
	protected Boolean getBoolean(BukkitVariables globalParameters) {
		return globalParameters.offlinePlayer.isWhitelisted();
	}

}
