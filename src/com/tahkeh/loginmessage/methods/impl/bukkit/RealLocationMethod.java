package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.OriginalCastedMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class RealLocationMethod extends OriginalCastedMethod<BukkitVariables, PlayerVariables> {

	private final String type;
	private final Message message;

	public RealLocationMethod(final String type, final Message message) {
		super(type, PlayerVariables.class);
		this.type = type;
		this.message = message;
	}

	@Override
	protected StringParameterType call(PlayerVariables globalParameters) {
		return new StringParameterType(this.message.getLocation(this.type, globalParameters.offlinePlayer.getName()));
	}
}
