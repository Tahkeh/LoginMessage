package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

public class AliasMethod implements Method {

	private final String result;
	private final int paramCount;

	public AliasMethod(final String result, final int paramCount) {
		this.result = result;
		this.paramCount = paramCount;
	}

	@Override
	public String call(OfflinePlayer player, String event, String... parameters) {
		if (this.paramCount == parameters.length) {
			String result = this.result;
			for (int i = 0; i < parameters.length; i++) {
				result = result.replaceAll("\\$" + i + ";", parameters[i]);
			}
			return result;
		} else {
			return null;
		}
	}

	@Override
	public boolean recursive() {
		return true;
	}

}
