package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class LastLoginMethod extends OriginalMethod<BukkitVariables> {

	private final Message message;

	public LastLoginMethod(final Message message) {
		super("laston");
		this.message = message;
	}

	@Override
	protected String call(BukkitVariables globalParameters) {
		return this.message.getTimeDifference(this.message.getLastLogin(globalParameters.offlinePlayer.getName()));
	}

}
