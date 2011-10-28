package com.tahkeh.loginmessage.methods;

import org.bukkit.entity.Player;

public class IfSetMethod extends IfMethod {

	public IfSetMethod(final boolean inverted) {
		super(1, inverted);
	}

	@Override
	protected boolean match(Player player, String event, String... preValues) {
		return preValues != null;
	}
}
