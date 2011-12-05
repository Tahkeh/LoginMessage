package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public final class ConstantMethod extends OriginalMethod {

	private final String value;

	public ConstantMethod(final String value) {
		this.value = value;
	}

	@Override
	protected String call(OfflinePlayer player, String event, Variables globalParameters) {
		return this.value;
	}

}
