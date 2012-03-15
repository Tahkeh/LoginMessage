package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.CaseMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class StatusMethod extends CaseMethod<BukkitVariables, PlayerVariables> {

	private final Message message;

	public StatusMethod(final Message message) {
		super(0, "status", PlayerVariables.class);
		this.message = message;
	}

	@Override
	protected String call(final Parameter[] preValues, PlayerVariables globalParameters) {
		return this.message.getStatus(globalParameters.offlinePlayer);
	}

}
