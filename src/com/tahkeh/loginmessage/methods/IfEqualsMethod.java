package com.tahkeh.loginmessage.methods;

import org.bukkit.entity.Player;

public class IfEqualsMethod extends IfMethod {

	public IfEqualsMethod(final boolean inverted) {
		super(2, inverted);
	}

	@Override
	protected boolean match(Player player, String event, String... preValues) {
	    return preValues[0] != null && preValues[0].equals(preValues[1]);
	}
}
