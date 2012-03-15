package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.OriginalCastedMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class NameMethod extends OriginalCastedMethod<BukkitVariables, PlayerVariables> {

	public NameMethod() {
		super("nm", PlayerVariables.class);
	}

	@Override
	protected StringParameterType call(PlayerVariables globalParameters) {
		return new StringParameterType(globalParameters.offlinePlayer.getName());
	}

}
