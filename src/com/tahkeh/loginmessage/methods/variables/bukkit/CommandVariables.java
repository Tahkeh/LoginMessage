package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CommandVariables extends BukkitVariables {

	public static final String NAME = "command";

	public final String command;
	public final Player trigger;

	public CommandVariables(final String command, final Player trigger, final OfflinePlayer offlinePlayer) {
		super(false, CommandVariables.NAME, offlinePlayer);
		this.command = command;
		this.trigger = trigger;
	}

}
