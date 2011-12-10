package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class RawTimeMethod extends OriginalPlayerMethod {

	public RawTimeMethod() {
		super("rtime");
	}

	@Override
	protected String call(Player player, Variables globalParameters) {
		return Long.toString(player.getWorld().getTime());
	}

}
