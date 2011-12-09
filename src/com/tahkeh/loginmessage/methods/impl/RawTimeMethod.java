package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;

public class RawTimeMethod extends OriginalPlayerMethod {

	public RawTimeMethod() {
		super("rtime");
	}

	@Override
	protected String call(Player player, String event) {
		return Long.toString(player.getWorld().getTime());
	}

}
