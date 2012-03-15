package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

public class QuitVariables extends PlayerVariables {

	public static final String NAME = "quit";

	public QuitVariables(final OfflinePlayer offlinePlayer) {
		super(true, QuitVariables.NAME, offlinePlayer);
	}
}
