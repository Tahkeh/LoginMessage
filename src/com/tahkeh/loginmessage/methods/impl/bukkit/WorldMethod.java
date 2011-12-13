package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class WorldMethod extends OriginalPlayerMethod {

	public WorldMethod() {
		super("world");
	}

	@Override
	protected String call(Player player, BukkitVariables globalParameters) {
		return player.getWorld().getName();
	}

}
