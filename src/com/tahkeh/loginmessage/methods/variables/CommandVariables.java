package com.tahkeh.loginmessage.methods.variables;

import org.bukkit.entity.Player;

public class CommandVariables extends Variables {

	public static final String NAME = "command";

	public final String command;
	public final Player trigger;

	public CommandVariables(final String command, final Player trigger) {
		super(false, CommandVariables.NAME);
		this.command = command;
		this.trigger = trigger;
	}

}
