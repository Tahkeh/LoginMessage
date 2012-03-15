package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

public class ListVariables extends PlayerVariables {

	public static final String NAME = "list";

	public ListVariables(final OfflinePlayer offlinePlayer) {
		super(false, ListVariables.NAME, offlinePlayer);
	}

}
