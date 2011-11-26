package com.tahkeh.loginmessage.methods.variables;

import org.bukkit.entity.Player;

public class KickVariables extends DefaultVariables {

	public final String reason;

	public KickVariables(final String reason, final Player trigger) {
		super(trigger);
		this.reason = reason;
	}
}
