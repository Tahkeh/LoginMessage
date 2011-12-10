package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public final class ConstantMethod extends OriginalMethod {

	private final String value;

	public ConstantMethod(final String value, final String defaultName) {
		super(defaultName);
		this.value = value;
	}

	@Override
	protected String call(OfflinePlayer player, Variables globalParameters) {
		return this.value;
	}

}
