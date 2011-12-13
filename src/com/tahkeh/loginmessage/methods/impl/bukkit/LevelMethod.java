package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class LevelMethod extends OriginalPlayerMethod {

	public LevelMethod() {
		super("level");
	}

	@Override
	protected String call(Player player, BukkitVariables globalParameters) {
		return Integer.toString(player.getLevel());
	}

}
