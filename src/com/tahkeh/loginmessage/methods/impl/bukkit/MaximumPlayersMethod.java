package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.Bukkit;

import com.tahkeh.loginmessage.methods.EmptyMethod;

public class MaximumPlayersMethod extends EmptyMethod {

	public MaximumPlayersMethod() {
		super("max");
	}

	@Override
	protected String call() {
		return Integer.toString(Bukkit.getServer().getMaxPlayers());
	}

}
