package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class RedirectMethod implements Method {

	private final Method redirected;

	public RedirectMethod(final Method redirected) {
		this.redirected = redirected;
	}

	@Override
	public String call(OfflinePlayer player, Parameter[] parameters, Variables globalParameters) {
		return this.redirected.call(player, parameters, globalParameters);
	}

	@Override
	public boolean isRecursive() {
		return this.redirected.isRecursive();
	}
}
