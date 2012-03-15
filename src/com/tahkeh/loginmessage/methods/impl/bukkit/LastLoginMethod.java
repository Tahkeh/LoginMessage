package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.OriginalCastedMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class LastLoginMethod extends OriginalCastedMethod<BukkitVariables, PlayerVariables> {

	private final Message message;

	public LastLoginMethod(final Message message) {
		super("laston", PlayerVariables.class);
		this.message = message;
	}

	@Override
	protected StringParameterType call(PlayerVariables globalParameters) {
		return new StringParameterType(this.message.getTimeDifference(this.message.getLastLogin(globalParameters.offlinePlayer.getName())));
	}

}
