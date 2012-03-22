package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CommandVariables extends PlayerVariables implements KeyVariables {

	public static final String NAME = "command";

	public final String command;
	public final Player trigger;

	public CommandVariables(final String command, final Player trigger, final OfflinePlayer offlinePlayer) {
		super(false, CommandVariables.NAME, offlinePlayer);
		this.command = command;
		this.trigger = trigger;
	}

	@Override
	public String[] getKeys() {
		return new String[] { command };
	}
}
