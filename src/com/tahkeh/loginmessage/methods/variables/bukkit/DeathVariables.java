package com.tahkeh.loginmessage.methods.variables.bukkit;

import org.bukkit.OfflinePlayer;

public class DeathVariables extends BukkitVariables {

	public static final String NAME = "death";

	public final String item;
	public final String entity;
	public final String key;

	public DeathVariables(final String item, final String entity, final String key, final OfflinePlayer offlinePlayer) {
		super(false, DeathVariables.NAME, offlinePlayer);
		this.item = item;
		this.entity = entity;
		this.key = key;
	}
}
