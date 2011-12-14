package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

public class LoginVariables extends BukkitVariables {

	public static final String NAME = "login";

	public LoginVariables(final OfflinePlayer offlinePlayer) {
		super(false, LoginVariables.NAME, offlinePlayer);
	}

}
