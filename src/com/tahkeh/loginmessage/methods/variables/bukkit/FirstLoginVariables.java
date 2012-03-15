package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

public class FirstLoginVariables extends PlayerVariables {

	public static final String NAME = "firstlogin";

	public FirstLoginVariables(final OfflinePlayer offlinePlayer) {
		super(false, FirstLoginVariables.NAME, offlinePlayer);
	}

}
