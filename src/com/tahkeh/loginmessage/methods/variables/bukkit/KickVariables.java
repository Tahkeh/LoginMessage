package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

public class KickVariables extends BukkitVariables {

	public static final String NAME = "kick";

	public final String reason;

	public KickVariables(final String reason, final OfflinePlayer offlinePlayer) {
		super(true, KickVariables.NAME, offlinePlayer);
		this.reason = reason;
	}
}
