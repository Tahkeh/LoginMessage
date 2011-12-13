package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class RealLocationMethod extends OriginalMethod<BukkitVariables> {

	private final String type;
	private final Message message;

	public RealLocationMethod(final String type, final Message message) {
		super(type);
		this.type = type;
		this.message = message;
	}

	@Override
	protected String call(BukkitVariables globalParameters) {
		return this.message.getLocation(this.type, globalParameters.offlinePlayer.getName());
	}
}
