package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

public class PlayerNotFoundVariables extends BukkitVariables {

	public static final String NAME = "playernotfound";

	public final String targetName;

	public PlayerNotFoundVariables(final String targetName, final OfflinePlayer offlinePlayer) {
		super(false, PlayerNotFoundVariables.NAME, offlinePlayer);
		this.targetName = targetName;
	}

}
