package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;

public class CurrentExperienceMethod extends OriginalPlayerMethod {

	public CurrentExperienceMethod() {
		super("curxp");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String call(Player player, String event) {
		try {
			return Float.toString(player.getExp());
		} catch (NoSuchMethodError e) {
			return Integer.toString(player.getExperience());
		}
	}

}
