package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class RawTimeMethod extends OriginalPlayerMethod {

	public RawTimeMethod() {
		super("rtime");
	}

	@Override
	protected String call(Player player, BukkitVariables globalParameters) {
		return Long.toString(player.getWorld().getTime());
	}

}
