package com.tahkeh.loginmessage.methods.variables;

public class DeathVariables extends Variables {

	public static final String NAME = "death";

	public final String item;
	public final String entity;
	public final String key;

	public DeathVariables(final String item, final String entity, final String key) {
		super(false, DeathVariables.NAME);
		this.item = item;
		this.entity = entity;
		this.key = key;
	}
}
