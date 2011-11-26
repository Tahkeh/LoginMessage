package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

public class IfSetMethod extends IfMethod {

	public IfSetMethod(final boolean inverted) {
		super(1, inverted);
	}

	@Override
	protected Boolean match(OfflinePlayer player, String event, String[] preValues) {
		return preValues[0] != null;
	}
}
