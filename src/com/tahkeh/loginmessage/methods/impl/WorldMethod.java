package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class WorldMethod extends OriginalPlayerMethod {

	public WorldMethod() {
		super("world");
	}

	@Override
	protected String call(Player player, Variables globalParameters) {
		return player.getWorld().getName();
	}

}
