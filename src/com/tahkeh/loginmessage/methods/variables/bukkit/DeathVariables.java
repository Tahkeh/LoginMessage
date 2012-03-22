package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

public class DeathVariables extends PlayerVariables implements KeyVariables {

	public static final String NAME = "death";

	public final String item;
	public final String entity;
	private final String[] keys;

	public DeathVariables(final String item, final String entity, final String[] keys, final OfflinePlayer offlinePlayer) {
		super(false, DeathVariables.NAME, offlinePlayer);
		this.item = item;
		this.entity = entity;
		this.keys = keys.clone();
	}

	@Override
	public String[] getKeys() {
		return keys.clone();
	}
}
