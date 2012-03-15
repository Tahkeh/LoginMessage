package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

public class PlayerVariables extends BukkitVariables {

	public final OfflinePlayer offlinePlayer;

	public PlayerVariables(boolean leaveEvent, String name, OfflinePlayer offlinePlayer) {
		super(leaveEvent, name);
		this.offlinePlayer = offlinePlayer;
	}

}
