package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public class RedirectMethod implements Method {

	private final Method redirected;

	public RedirectMethod(final Method redirected) {
		this.redirected = redirected;
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, DefaultVariables globalParameters) {
		return this.redirected.call(player, event, parameters, globalParameters);
	}

	@Override
	public boolean recursive() {
		return this.redirected.recursive();
	}
}
