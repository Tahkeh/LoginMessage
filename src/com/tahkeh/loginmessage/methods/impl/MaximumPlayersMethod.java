package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class MaximumPlayersMethod extends OriginalMethod {

	public MaximumPlayersMethod() {
		super("max");
	}

	@Override
	protected String call(OfflinePlayer player, String event, Variables globalParameters) {
		return Integer.toString(Bukkit.getServer().getMaxPlayers());
	}

}
