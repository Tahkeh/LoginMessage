package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.variables.Variables;

/**
 * Minimum variables for LoginMessage and Bukkit related methods.
 */
public class BukkitVariables implements Variables {

	public final boolean leaveEvent;
	public final String name;
	public final OfflinePlayer offlinePlayer;

	protected BukkitVariables(final boolean leaveEvent, final String name, final OfflinePlayer offlinePlayer) {
		this.leaveEvent = leaveEvent;
		this.name = name;
		this.offlinePlayer = offlinePlayer;
	}
}
