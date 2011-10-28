package com.tahkeh.loginmessage.methods;

import org.bukkit.entity.Player;

public class RedirectMethod implements Method {

	private final Method redirected;

	public RedirectMethod(final Method redirected) {
		this.redirected = redirected;
	}

	@Override
	public String call(Player player, String event, String... parameters) {
		return this.redirected.call(player, event, parameters);
	}

	@Override
	public boolean recursive() {
		return this.redirected.recursive();
	}
}
