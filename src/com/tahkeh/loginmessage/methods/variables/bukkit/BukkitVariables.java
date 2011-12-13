package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.variables.Variables;

public class BukkitVariables implements Variables {

	public final boolean leaveEvent;
	public final String name;
	public final OfflinePlayer offlinePlayer;

	// Default Variables as some kind of singletons
	public final static String QUIT_NAME = "quit";
	public final static String FIRST_LOGIN_NAME = "firstlogin";
	public final static String LOGIN_NAME = "login";
	public final static String INTERVAL_NAME = "interval";
	public final static String LIST_NAME = "list";

	protected BukkitVariables(final boolean leaveEvent, final String name, final OfflinePlayer offlinePlayer) {
		this.leaveEvent = leaveEvent;
		this.name = name;
		this.offlinePlayer = offlinePlayer;
	}

	public static BukkitVariables createQuit(final OfflinePlayer offlinePlayer) {
		return new BukkitVariables(true, QUIT_NAME, offlinePlayer);
	}

	public static BukkitVariables createLogin(final OfflinePlayer offlinePlayer) {
		return new BukkitVariables(true, LOGIN_NAME, offlinePlayer);
	}

	public static BukkitVariables createFirstLogin(final OfflinePlayer offlinePlayer) {
		return new BukkitVariables(true, FIRST_LOGIN_NAME, offlinePlayer);
	}

	public static BukkitVariables createInterval(final OfflinePlayer offlinePlayer) {
		return new BukkitVariables(true, INTERVAL_NAME, offlinePlayer);
	}

	public static BukkitVariables createList(final OfflinePlayer offlinePlayer) {
		return new BukkitVariables(true, LIST_NAME, offlinePlayer);
	}
}
