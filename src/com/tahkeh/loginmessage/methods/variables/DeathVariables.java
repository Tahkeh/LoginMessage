package com.tahkeh.loginmessage.methods.variables;

import org.bukkit.entity.Player;

public class DeathVariables extends Variables {

	public final String item;
	public final String entity;

	public DeathVariables(final String item, final String entity, final Player trigger) {
		super(trigger);
		this.item = item;
		this.entity = entity;
	}
}
