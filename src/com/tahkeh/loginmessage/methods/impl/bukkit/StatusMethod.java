package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.CaseMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class StatusMethod extends CaseMethod<BukkitVariables> {

	private final Message message;

	public StatusMethod(final Message message) {
		super("status");
		this.message = message;
	}

	@Override
	protected String call(BukkitVariables globalParameters) {
		return this.message.getStatus(globalParameters.offlinePlayer);
	}

}
