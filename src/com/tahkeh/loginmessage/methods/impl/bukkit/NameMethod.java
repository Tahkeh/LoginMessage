package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class NameMethod extends OriginalMethod<BukkitVariables> {

	public NameMethod() {
		super("nm");
	}

	@Override
	protected String call(BukkitVariables globalParameters) {
		return globalParameters.offlinePlayer.getName();
	}

}
