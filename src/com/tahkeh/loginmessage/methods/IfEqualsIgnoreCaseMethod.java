package com.tahkeh.loginmessage.methods;

import org.bukkit.entity.Player;

public class IfEqualsIgnoreCaseMethod extends IfMethod {

	protected IfEqualsIgnoreCaseMethod(boolean inverted) {
		super(2, inverted);
	}

	@Override
	protected boolean match(Player player, String event, String... preValues) {
		return preValues[0] != null && preValues[0].equalsIgnoreCase(preValues[1]);
	}
}
