package com.tahkeh.loginmessage.methods.variables.bukkit;

import com.tahkeh.loginmessage.methods.variables.Variables;

/**
 * Minimum variables for LoginMessage and Bukkit related methods.
 */
public class BukkitVariables implements Variables {

	public final boolean leaveEvent;
	public final String name;

	protected BukkitVariables(final boolean leaveEvent, final String name) {
		this.leaveEvent = leaveEvent;
		this.name = name;
	}
}
